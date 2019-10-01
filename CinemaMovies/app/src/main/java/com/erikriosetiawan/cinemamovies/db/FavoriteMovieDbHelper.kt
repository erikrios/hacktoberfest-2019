package com.erikriosetiawan.cinemamovies.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import android.util.Log
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract.FavoriteMovieEntry
import com.erikriosetiawan.cinemamovies.model.Movie

class FavoriteMovieDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "favoritemovie.db"
        const val DATABASE_VERSION = 1
        val LOG_TAG = FavoriteMovieDbHelper::class.java.simpleName
    }

    lateinit var db: SQLiteDatabase
    lateinit var dbHandler: SQLiteOpenHelper

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_FAVORITE_MOVIES_TABLE =
            "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                    FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavoriteMovieEntry.COLUMN_ID + " INTEGER, " +
                    FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + ");"

        db?.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME)
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

    fun addFavorite(movie: Movie) {
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(FavoriteMovieEntry.COLUMN_ID, movie.id)
        values.put(FavoriteMovieEntry.COLUMN_TITLE, movie.title)
        values.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate)
        values.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.posterPath)
        values.put(FavoriteMovieEntry.COLUMN_OVERVIEW, movie.overview)
        values.put(FavoriteMovieEntry.COLUMN_VOTE_COUNT, movie.voteCount)
        values.put(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, movie.voteAverage)

        db.insert(FavoriteMovieEntry.TABLE_NAME, null, values)
        db.close()
        Log.d(LOG_TAG, "Sukses menambahkan ke favorite")
    }

    fun deleteFavorite(id: String) {
        val db = this.writableDatabase
        db.delete(FavoriteMovieEntry.TABLE_NAME, FavoriteMovieEntry.COLUMN_ID + "=" + id, null)
        Log.d(LOG_TAG, "Sukses menghapus favorite")
    }

    fun getAllFavorite(): MutableList<Movie> {
        val columns = arrayOf(
            FavoriteMovieEntry._ID,
            FavoriteMovieEntry.COLUMN_ID,
            FavoriteMovieEntry.COLUMN_TITLE,
            FavoriteMovieEntry.COLUMN_RELEASE_DATE,
            FavoriteMovieEntry.COLUMN_POSTER_PATH,
            FavoriteMovieEntry.COLUMN_OVERVIEW,
            FavoriteMovieEntry.COLUMN_VOTE_COUNT,
            FavoriteMovieEntry.COLUMN_VOTE_AVERAGE
        )
        val sortOrder = FavoriteMovieEntry._ID + " ASC"
        val favoriteList: MutableList<Movie> = mutableListOf()

        val db: SQLiteDatabase = this.readableDatabase

        val cursor: Cursor =
            db.query(FavoriteMovieEntry.TABLE_NAME, columns, null, null, null, null, sortOrder)

        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(null, null, null, null, null, null, null, null)
                if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ID))) && TextUtils.isDigitsOnly(
                        cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ID))
                    )
                ) movie.id = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ID))
                else movie.id = "0"
                movie.title =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_TITLE))
                movie.releaseDate =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RELEASE_DATE))
                movie.posterPath =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POSTER_PATH))
                movie.overview =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_OVERVIEW))
                movie.voteCount =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VOTE_COUNT))
                movie.voteAverage =
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE))

                favoriteList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return favoriteList
    }
}