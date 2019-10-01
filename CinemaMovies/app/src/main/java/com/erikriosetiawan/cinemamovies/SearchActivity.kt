package com.erikriosetiawan.cinemamovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikriosetiawan.cinemamovies.adapter.MovieAdapter
import com.erikriosetiawan.cinemamovies.adapter.TvShowAdapter
import com.erikriosetiawan.cinemamovies.api.ApiRepository
import com.erikriosetiawan.cinemamovies.api.TheMovieDBApi
import com.erikriosetiawan.cinemamovies.model.Movie
import com.erikriosetiawan.cinemamovies.model.MovieResponse
import com.erikriosetiawan.cinemamovies.model.TvShow
import com.erikriosetiawan.cinemamovies.model.TvShowResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SearchActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private val tvShows = mutableListOf<TvShow>()
    private lateinit var apiKey: String
    private lateinit var language: String
    private lateinit var typeIntent: String
    private lateinit var query: String

    companion object {
        const val SEARCH_KEY = "Eous3G6ImV"
        const val SEARCH_MOVIE_KEY = "kjSdUiuo90"
        const val SEARCH_TV_SHOW_KEY = "bA95G3G0sL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        typeIntent = intent.getStringExtra(SEARCH_KEY)
        query = getQueryIntent()

        apiKey = BuildConfig.TMDB_API_KEY
        language = "en-US"
        Log.d("TES123", query)

        doAsync {
            val apiRepository = ApiRepository()
            val gson = Gson()
            if (typeIntent == SEARCH_MOVIE_KEY) {
                val data = gson.fromJson(
                    apiRepository.doRequest(
                        TheMovieDBApi.getMovieSearch(apiKey, language, query)
                    ), MovieResponse::class.java
                )
                uiThread {
                    movies.addAll(data.results!!)
                    setRecyclerView(typeIntent)
                }
            } else {
                val data = gson.fromJson(
                    apiRepository.doRequest(
                        TheMovieDBApi.getTvShowSearch(apiKey, language, query)
                    ), TvShowResponse::class.java
                )
                uiThread {
                    tvShows.addAll(data.results!!)
                    setRecyclerView(typeIntent)
                }
            }
        }
    }

    private fun setRecyclerView(key: String) {
        val rvSearch = findViewById<RecyclerView>(R.id.rv_search)
        rvSearch.layoutManager = LinearLayoutManager(this)
        if (key == SEARCH_MOVIE_KEY) rvSearch.adapter =
            MovieAdapter(this, movies) else rvSearch.adapter = TvShowAdapter(this, tvShows)
    }

    private fun getQueryIntent(): String {
        return when (typeIntent) {
            SEARCH_MOVIE_KEY -> intent.getStringExtra(SEARCH_MOVIE_KEY)
            else -> intent.getStringExtra(SEARCH_TV_SHOW_KEY)
        }
    }
}
