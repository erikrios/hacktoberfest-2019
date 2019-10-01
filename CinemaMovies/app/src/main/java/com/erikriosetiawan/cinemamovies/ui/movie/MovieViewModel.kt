package com.erikriosetiawan.cinemamovies.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.cinemamovies.BuildConfig
import com.erikriosetiawan.cinemamovies.api.ApiRepository
import com.erikriosetiawan.cinemamovies.api.TheMovieDBApi
import com.erikriosetiawan.cinemamovies.model.Movie
import com.erikriosetiawan.cinemamovies.model.MovieResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MovieViewModel : ViewModel() {

    private var mutableLiveDataMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var isFetching = MutableLiveData<Boolean>()
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val language = "en-US"

    fun getMovies(): LiveData<List<Movie>> {
        return mutableLiveDataMovies
    }

    fun getIsFetching(): LiveData<Boolean> {
        return isFetching
    }

    private fun setIsFetching(isFetching: Boolean) {
        this.isFetching.postValue(isFetching)
    }

    fun open() {
        setIsFetching(true)
        doAsync {
            val apiRepository = ApiRepository()
            val gson = Gson()
            val data = gson.fromJson(
                apiRepository.doRequest(
                    TheMovieDBApi.getMovie(apiKey, language)
                ), MovieResponse::class.java
            )
            uiThread {
                mutableLiveDataMovies.value = data.results
            }
        }
    }

    fun close() {
        if (mutableLiveDataMovies.value != null) {
            setIsFetching(false)
        }
    }
}