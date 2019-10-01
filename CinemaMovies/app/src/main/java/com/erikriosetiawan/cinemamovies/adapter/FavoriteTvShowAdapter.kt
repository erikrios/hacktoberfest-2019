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
import com.erikriosetiawan.cinemamovies.model.TvShow
import com.squareup.picasso.Picasso

class FavoriteTvShowAdapter(
    private val context: Context,
    private val favoriteTvShows: List<TvShow>
) : RecyclerView.Adapter<FavoriteTvShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false))

    override fun getItemCount(): Int = favoriteTvShows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(favoriteTvShows[position]) {
            val dataIntent = Intent(context, DetailActivity::class.java)
            dataIntent.putExtra(DetailActivity.KEY, DetailActivity.TV_SHOW_KEY)
            dataIntent.putExtra(DetailActivity.TV_SHOW_KEY, favoriteTvShows[position])
            context.startActivity(dataIntent)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private val tvReleaseDate: TextView = view.findViewById(R.id.tv_release_date)
        private val tvTitle: TextView = view.findViewById(R.id.tv_title)
        private val tvOverview: TextView = view.findViewById(R.id.tv_overview)

        fun bindItem(favoriteTvShow: TvShow, listener: (TvShow) -> Unit) {
            Picasso.get().load("https://image.tmdb.org/t/p/w185${favoriteTvShow.posterPath}")
                .into(imgPoster)
            tvReleaseDate.text = favoriteTvShow.firstAirDate
            tvTitle.text = favoriteTvShow.name
            tvOverview.text = favoriteTvShow.overview

            itemView.setOnClickListener { listener(favoriteTvShow) }
        }
    }
}