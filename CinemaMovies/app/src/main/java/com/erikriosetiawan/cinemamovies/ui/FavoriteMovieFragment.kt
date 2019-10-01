package com.erikriosetiawan.cinemamovies.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.adapter.FavoriteMovieAdapter
import com.erikriosetiawan.cinemamovies.db.FavoriteMovieDbHelper
import com.erikriosetiawan.cinemamovies.model.Movie

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private val favoriteMovies: MutableList<Movie> = mutableListOf()
    private lateinit var favoriteMovieDbHelper: FavoriteMovieDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_favorite_movie, container, false)

        favoriteMovieDbHelper = FavoriteMovieDbHelper(root.context)
        favoriteMovies.clear()
        favoriteMovies.addAll(favoriteMovieDbHelper.getAllFavorite())

        setRecyclerView(root)
        return root
    }

    private fun setRecyclerView(root: View) {
        val rvFavoriteMovie = root.findViewById<RecyclerView>(R.id.rv_movie)
        rvFavoriteMovie.layoutManager = LinearLayoutManager(root.context)
        rvFavoriteMovie.adapter = FavoriteMovieAdapter(root.context, favoriteMovies)
    }
}