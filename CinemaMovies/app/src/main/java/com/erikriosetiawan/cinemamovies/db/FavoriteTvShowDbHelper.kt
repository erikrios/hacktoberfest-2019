package com.erikriosetiawan.cinemamovies.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import android.util.Log
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract.FavoriteTvShowEntry
import com.erikriosetiawan.cinemamovies.model.TvShow

class FavoriteTvShowDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "favoritetvshow.db"
        const val DATABASE_VERSION = 1
        val LOG_TAG = FavoriteTvShowDbHelper::class.java.simpleName

    }

    lateinit var db: SQLiteDatabase
    lateinit var dbHandler: SQLiteOpenHelper

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_FAVORITE_TV_SHOW_TABLE =
            "CREATE TABLE " + FavoriteTvShowEntry.TABLE_NAME + " (" +
                    FavoriteTvShowEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FavoriteTvShowEntry.COLUMN_ID + " INTEGER, " +
                    FavoriteTvShowEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    FavoriteTvShowEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    FavoriteTvShowEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    FavoriteTvShowEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    FavoriteTvShowEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL, " +
                    FavoriteTvShowEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + ");"

        db?.execSQL(SQL_CREATE_FAVORITE_TV_SHOW_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + FavoriteTvShowEntry.TABLE_NAME)
        onCreate(db)
    }

    fun opens() {
        db = dbHandler.writableDatabase
        Log.i(LOG_TAG, "Database Terbuka")
    }

    fun closes() {
        dbHandler.close()
        Log.i(LOG_TAG, "Database Tertutup")
    }

    fun addFavorite(tvShow: TvShow) {
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(FavoriteTvShowEntry.COLUMN_ID, tvShow.id)
        values.put(FavoriteTvShowEntry.COLUMN_TITLE, tvShow.name)
        values.put(FavoriteTvShowEntry.COLUMN_RELEASE_DATE, tvShow.firstAirDate)
        values.put(FavoriteTvShowEntry.COLUMN_POSTER_PATH, tvShow.posterPath)
        values.put(FavoriteTvShowEntry.COLUMN_OVERVIEW, tvShow.overview)
        values.put(FavoriteTvShowEntry.COLUMN_VOTE_COUNT, tvShow.voteCount)
        values.put(FavoriteTvShowEntry.COLUMN_VOTE_AVERAGE, tvShow.voteAverage)

        db.insert(FavoriteTvShowEntry.TABLE_NAME, null, values)
        db.close()
        Log.d(LOG_TAG, "Sukses menambahkan ke favorite")
    }

    fun deleteFavorite(id: String) {
        val db = this.writableDatabase
        db.delete(FavoriteTvShowEntry.TABLE_NAME, FavoriteTvShowEntry.COLUMN_ID + "=" + id, null)
        Log.d(LOG_TAG, "Sukses menghapus favorite")
    }

    fun getAllFavorite(): MutableList<TvShow> {
        val columns = arrayOf(
            FavoriteTvShowEntry._ID,
            FavoriteTvShowEntry.COLUMN_ID,
            FavoriteTvShowEntry.COLUMN_TITLE,
            FavoriteTvShowEntry.COLUMN_RELEASE_DATE,
            FavoriteTvShowEntry.COLUMN_POSTER_PATH,
            FavoriteTvShowEntry.COLUMN_OVERVIEW,
            FavoriteTvShowEntry.COLUMN_VOTE_COUNT,
            FavoriteTvShowEntry.COLUMN_VOTE_AVERAGE
        )
        val sortOrder = FavoriteTvShowEntry._ID + " ASC"
        val favoriteList: MutableList<TvShow> = mutableListOf()

        val db: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor =
            db.query(FavoriteTvShowEntry.TABLE_NAME, columns, null, null, null, null, sortOrder)

        if (cursor.moveToFirst()) {
            do {
                val tvShow = TvShow(null, null, null, null, null, null, null, null)
                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_ID))) && TextUtils.isDigitsOnly(
                        cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_ID))
                    )
                ) tvShow.id = cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_ID))
                else tvShow.id = "0"
                tvShow.name =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_TITLE))
                tvShow.firstAirDate =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_RELEASE_DATE))
                tvShow.posterPath =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_POSTER_PATH))
                tvShow.overview =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_OVERVIEW))
                tvShow.voteCount =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_VOTE_COUNT))
                tvShow.voteAverage =
                    cursor.getString(cursor.getColumnIndex(FavoriteTvShowEntry.COLUMN_VOTE_AVERAGE))

                favoriteList.add(tvShow)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return favoriteList
    }
}