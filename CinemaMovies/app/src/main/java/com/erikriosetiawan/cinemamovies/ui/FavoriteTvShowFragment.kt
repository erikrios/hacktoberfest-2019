package com.erikriosetiawan.cinemamovies.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.adapter.FavoriteTvShowAdapter
import com.erikriosetiawan.cinemamovies.db.FavoriteTvShowDbHelper
import com.erikriosetiawan.cinemamovies.model.TvShow

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvShowFragment : Fragment() {

    private val favoriteTvShows: MutableList<TvShow> = mutableListOf()
    private lateinit var favoriteTvShowDbHelper: FavoriteTvShowDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)

        favoriteTvShowDbHelper = FavoriteTvShowDbHelper(root.context)
        favoriteTvShows.clear()
        favoriteTvShows.addAll(favoriteTvShowDbHelper.getAllFavorite())

        setRecyclerView(root)
        return root
    }

    private fun setRecyclerView(root: View) {
        val rvFavoriteTvShow = root.findViewById<RecyclerView>(R.id.rv_tv_show)
        rvFavoriteTvShow.layoutManager = LinearLayoutManager(root.context)
        rvFavoriteTvShow.adapter = FavoriteTvShowAdapter(root.context, favoriteTvShows)
    }
}