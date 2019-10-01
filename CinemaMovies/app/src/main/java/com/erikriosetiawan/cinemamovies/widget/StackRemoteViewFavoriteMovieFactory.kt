package com.erikriosetiawan.cinemamovies.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract
import com.erikriosetiawan.cinemamovies.db.FavoriteDbContract.FavoriteMovieEntry
import com.erikriosetiawan.cinemamovies.db.FavoriteMovieDbHelper
import com.erikriosetiawan.cinemamovies.model.Movie
import java.lang.Exception

class StackRemoteViewFavoriteMovieFactory(context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var favoriteMovieDbHelper: FavoriteMovieDbHelper
    private var database: SQLiteDatabase
    private var mContext: Context = context
    private lateinit var cursor: Cursor
    private val favoriteMovie: MutableList<Movie> = mutableListOf()

    init {
        favoriteMovieDbHelper = FavoriteMovieDbHelper(context)
        database = favoriteMovieDbHelper.writableDatabase
    }

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {

        val identifyToken: Long = Binder.clearCallingIdentity()

        cursor = database.rawQuery(
            "SELECT * FROM " + FavoriteMovieEntry.TABLE_NAME,
            null
        )
        cursor.moveToFirst()
        var movie: Movie
        if (cursor.count > 0) {
            do {
                movie = Movie(null, null, null, null, null, null, null, null)
                movie.id =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_ID))
                movie.title =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_TITLE))
                movie.posterPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_POSTER_PATH))
                movie.overview =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_OVERVIEW))
                movie.releaseDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_RELEASE_DATE))
                movie.voteCount =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_VOTE_COUNT))
                movie.voteAverage =
                    cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE))

                favoriteMovie.add(movie)
                cursor.moveToNext()
            } while (!(cursor.isAfterLast))
        }
        cursor.close()

        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_favorite_movie_item)

        if (favoriteMovie.size > 0) {
            try {
                val bitmap: Bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w185" + favoriteMovie[position].posterPath)
                    .submit(512, 512)
                    .get()

                rv.setImageViewBitmap(R.id.img_favorite_movie_item, bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val extras = Bundle()
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position)

        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.img_favorite_movie_item, fillIntent)
        return rv
    }

    override fun getCount(): Int = favoriteMovie.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}