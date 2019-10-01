package com.erikriosetiawan.cinemamovies.api

import android.net.Uri
import com.erikriosetiawan.cinemamovies.BuildConfig

object TheMovieDBApi {

    fun getMovie(apiKey: String?, language: String?): String {
        return Uri.parse(
            BuildConfig.BASE_URL
        ).buildUpon()
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("api_key", apiKey)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getTvShow(apiKey: String?, language: String?): String {
        return Uri.parse(
            BuildConfig.BASE_URL
        ).buildUpon()
            .appendPath("discover")
            .appendPath("tv")
            .appendQueryParameter("api_key", apiKey)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getMovieSearch(apiKey: String?, language: String?, query: String?): String {
        return Uri.parse(
            BuildConfig.BASE_URL
        ).buildUpon()
            .appendPath("search")
            .appendPath("movie")
            .appendQueryParameter("api_key", apiKey)
            .appendQueryParameter("language", language)
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }

    fun getTvShowSearch(apiKey: String?, language: String?, query: String?): String {
        return Uri.parse(
            BuildConfig.BASE_URL
        ).buildUpon()
            .appendPath("search")
            .appendPath("tv")
            .appendQueryParameter("api_key", apiKey)
            .appendQueryParameter("language", language)
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }
}