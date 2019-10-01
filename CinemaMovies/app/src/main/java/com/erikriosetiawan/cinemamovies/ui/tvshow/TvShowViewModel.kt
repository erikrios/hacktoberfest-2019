package com.erikriosetiawan.cinemamovies.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erikriosetiawan.cinemamovies.BuildConfig
import com.erikriosetiawan.cinemamovies.api.ApiRepository
import com.erikriosetiawan.cinemamovies.api.TheMovieDBApi
import com.erikriosetiawan.cinemamovies.model.TvShow
import com.erikriosetiawan.cinemamovies.model.TvShowResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TvShowViewModel : ViewModel() {

    private var mutableLiveDataTvShows: MutableLiveData<List<TvShow>> = MutableLiveData()
    private var isFetching = MutableLiveData<Boolean>()
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val language = "en-US"

    fun getTvShows(): LiveData<List<TvShow>> {
        return mutableLiveDataTvShows
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
                    TheMovieDBApi.getTvShow(apiKey, language)
                ), TvShowResponse::class.java
            )
            uiThread {
                mutableLiveDataTvShows.value = data.results
            }
        }
    }

    fun close() {
        if (mutableLiveDataTvShows.value != null) {
            setIsFetching(false)
        }
    }
}