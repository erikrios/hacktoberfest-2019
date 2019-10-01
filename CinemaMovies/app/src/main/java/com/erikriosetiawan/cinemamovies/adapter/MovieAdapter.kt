package com.erikriosetiawan.cinemamovies.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erikriosetiawan.cinemamovies.DetailActivity
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(movies[position]) {
            val dataIntent = Intent(context, DetailActivity::class.java)
            dataIntent.putExtra(DetailActivity.KEY, DetailActivity.MOVIE_KEY)
            dataIntent.putExtra(DetailActivity.MOVIE_KEY, movies[position])
            context.startActivity(dataIntent)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private var tvReleaseDate: TextView = view.findViewById(R.id.tv_release_date)
        private var tvTitle: TextView = view.findViewById(R.id.tv_title)
        private var tvOverview: TextView = view.findViewById(R.id.tv_overview)

        fun bindItem(movie: Movie, listener: (Movie) -> Unit) {
            Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}").into(imgPoster)
            tvReleaseDate.text = movie.releaseDate
            tvTitle.text = movie.title
            tvOverview.text = movie.overview

            itemView.setOnClickListener { listener(movie) }
        }
    }

}