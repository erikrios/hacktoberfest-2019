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

class TvShowAdapter(private val context: Context, private val tvShows: List<TvShow>) :
    RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false))

    override fun getItemCount(): Int = tvShows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(tvShows[position]) {
            val dataIntent = Intent(context, DetailActivity::class.java)
            dataIntent.putExtra(DetailActivity.KEY, DetailActivity.TV_SHOW_KEY)
            dataIntent.putExtra(DetailActivity.TV_SHOW_KEY, tvShows[position])
            context.startActivity(dataIntent)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private var tvTitle: TextView = view.findViewById(R.id.tv_title)
        private var tvOverview: TextView = view.findViewById(R.id.tv_overview)
        private var tvReleaseDate: TextView = view.findViewById(R.id.tv_release_date)

        fun bindItem(tvShow: TvShow, listener: (TvShow) -> Unit) {
            Picasso.get().load("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")
                .into(imgPoster)
            tvTitle.text = tvShow.name
            tvOverview.text = tvShow.overview
            tvReleaseDate.text = tvShow.firstAirDate

            itemView.setOnClickListener { listener(tvShow) }
        }
    }
}