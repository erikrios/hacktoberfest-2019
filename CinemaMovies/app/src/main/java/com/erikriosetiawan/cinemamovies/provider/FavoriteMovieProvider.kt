package com.erikriosetiawan.cinemamovies.provider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract.FavoriteMovieEntry.Companion.COLUMN_TITLE
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract.FavoriteMovieEntry.Companion.TABLE_NAME
import com.erikriosetiawan.cinemamovies.db.FavoriteMovieDbHelper


class FavoriteMovieProvider : ContentProvider() {

    companion object {
        const val PROVIDER_NAME = "com.erikriosetiawan.cinemamovies.provider.FavoriteMovieProvider"
        const val URL = "content://$PROVIDER_NAME/$TABLE_NAME"
        val CONTENT_URI: Uri = Uri.parse(URL)

        val _ID = FavoriteDbContract.FavoriteMovieEntry._ID
        val TITLE = COLUMN_TITLE

        private val MOVIES_PROJECTION_MAP: HashMap<String, String> = HashMap()

        const val MOVIES = 1
        const val MOVIE_ID = 2

        private fun createUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            matcher.addURI(PROVIDER_NAME, TABLE_NAME, MOVIES)
            matcher.addURI(PROVIDER_NAME, "$TABLE_NAME/#", MOVIE_ID)

            return matcher
        }

        val uriMatcher = createUriMatcher()
    }

    private lateinit var database: SQLiteDatabase

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowId: Long = database.insert(TABLE_NAME, "", values)

        if (rowId > 0) {
            val myUri: Uri = ContentUris.withAppendedId(CONTENT_URI, rowId)
            context.contentResolver.notifyChange(myUri, null)
        }
        throw SQLException("Failed to add a record into $uri")
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_NAME

        when (uriMatcher.match(uri)) {
            MOVIES -> queryBuilder.setProjectionMap(MOVIES_PROJECTION_MAP)
            MOVIE_ID -> queryBuilder.appendWhere(_ID + "=" + uri.pathSegments[1])
        }

        val cursor: Cursor = queryBuilder.query(
            database,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        val context: Context = context
        val favoriteMoviesDbHelper = FavoriteMovieDbHelper(context)
        database = favoriteMoviesDbHelper.writableDatabase
        return database != null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val count: Int
        when (uriMatcher.match(uri)) {
            MOVIES -> count = database.update(TABLE_NAME, values, selection, selectionArgs)
            MOVIE_ID -> count = database.update(
                TABLE_NAME,
                values,
                _ID + " = " + uri.pathSegments[1] + if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count: Int
        when (uriMatcher.match(uri)) {
            MOVIES -> count = database.delete(TABLE_NAME, selection, selectionArgs)
            MOVIE_ID -> {
                val id: String = uri.pathSegments[1]
                count = database.delete(
                    TABLE_NAME,
                    _ID + " = " + id + if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            MOVIES -> "vnd.android.cursor.dir/vnd.example.$TABLE_NAME"
            MOVIE_ID -> "vnd.android.cursor.item/vnd.example.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unsupported URI:$uri")
        }
    }

}