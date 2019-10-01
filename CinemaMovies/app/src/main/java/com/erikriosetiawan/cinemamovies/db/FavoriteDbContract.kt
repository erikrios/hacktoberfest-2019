package com.erikriosetiawan.cinemamovies.db

import android.provider.BaseColumns

class FavoriteDbContract {

    open class KBaseColumns {
        val _ID = "_id"
    }

    class FavoriteMovieEntry : BaseColumns {

        companion object : KBaseColumns() {
            const val TABLE_NAME = "favoritemovies"
            const val COLUMN_ID = "movieid"
            const val COLUMN_TITLE = "title"
            const val COLUMN_POSTER_PATH = "posterpath"
            const val COLUMN_RELEASE_DATE = "releasedate"
            const val COLUMN_OVERVIEW = "overview"
            const val COLUMN_VOTE_COUNT = "votecount"
            const val COLUMN_VOTE_AVERAGE = "voteaverage"
        }
    }

    class FavoriteTvShowEntry : BaseColumns {

        companion object : KBaseColumns() {
            const val TABLE_NAME = "favoritetvshows"
            const val COLUMN_ID = "tvshowid"
            const val COLUMN_TITLE = "title"
            const val COLUMN_POSTER_PATH = "posterpath"
            const val COLUMN_RELEASE_DATE = "releasedate"
            const val COLUMN_OVERVIEW = "overview"
            const val COLUMN_VOTE_COUNT = "votecount"
            const val COLUMN_VOTE_AVERAGE = "voteaverage"
        }
    }
}