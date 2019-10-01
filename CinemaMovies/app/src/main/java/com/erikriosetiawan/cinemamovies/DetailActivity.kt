package com.erikriosetiawan.cinemamovies

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.erikriosetiawan.cinemamovies.db.FavoriteMovieDbHelper
import com.erikriosetiawan.cinemamovies.db.FavoriteTvShowDbHelper
import com.erikriosetiawan.cinemamovies.model.Movie
import com.erikriosetiawan.cinemamovies.model.TvShow
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KEY = "ScR850plM5"
        const val MOVIE_KEY = "Ab89KmLb43"
        const val TV_SHOW_KEY = "0bmLv85431"
    }

    private lateinit var imgPoster: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvPopularity: TextView
    private lateinit var tvOverview: TextView
    private lateinit var btnFavorite: MaterialFavoriteButton

    private lateinit var movie: Movie
    private lateinit var tvShow: TvShow

    private lateinit var favoriteMovieDbHelper: FavoriteMovieDbHelper
    private lateinit var favoriteTvShowDbHelper: FavoriteTvShowDbHelper
    private val activity: AppCompatActivity = this@DetailActivity
    val favoriteMovie = Movie(null, null, null, null, null, null, null, null)
    val favoriteTvShow = TvShow(null, null, null, null, null, null, null, null)

    private var movies = mutableListOf<Movie>()
    private var tvShows = mutableListOf<TvShow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()

        favoriteMovieDbHelper = FavoriteMovieDbHelper(activity)
        favoriteTvShowDbHelper = FavoriteTvShowDbHelper(activity)
        getDataIntent()
    }

    private fun initView() {
        imgPoster = findViewById(R.id.img_poster)
        tvTitle = findViewById(R.id.tv_title)
        tvReleaseDate = findViewById(R.id.tv_release_date)
        tvPopularity = findViewById(R.id.tv_popularity)
        tvOverview = findViewById(R.id.tv_overview)
        btnFavorite = findViewById(R.id.btn_favorite)
    }

    private fun getDataIntent() {
        when (intent.getStringExtra(KEY)) {
            MOVIE_KEY -> {
                movie = intent.getParcelableExtra(MOVIE_KEY)
                Picasso.get().load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
                    .into(imgPoster)
                tvTitle.text = movie.title
                tvReleaseDate.text = resources.getString(R.string.release_date, movie.releaseDate)
                tvPopularity.text = resources.getString(R.string.popularity, movie.popularity)
                tvOverview.text = movie.overview

                if (isFavoriteMovieExist(movie)) btnFavorite.isFavorite =
                    true else btnFavorite.isFavorite =
                    false

                btnFavorite.setOnFavoriteChangeListener { buttonView, favorite ->
                    if (!favorite) {
                        favoriteMovieDbHelper.deleteFavorite(movie.id!!)
                        btnFavorite.isFavorite = false
                    } else {
                        saveFavoriteMovie(
                            movie.title,
                            movie.releaseDate,
                            movie.voteCount,
                            movie.voteAverage,
                            movie.posterPath,
                            movie.overview,
                            movie.id
                        )
                        btnFavorite.isFavorite = true
                    }
                }
            }
            TV_SHOW_KEY -> {
                tvShow = intent.getParcelableExtra(TV_SHOW_KEY)
                Picasso.get().load("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")
                    .into(imgPoster)
                tvTitle.text = tvShow.name
                tvReleaseDate.text = resources.getString(R.string.release_date, tvShow.firstAirDate)
                tvPopularity.text = resources.getString(R.string.popularity, tvShow.popularity)
                tvOverview.text = tvShow.overview

                if (isFavoriteTvShowExists(tvShow)) btnFavorite.isFavorite =
                    true else btnFavorite.isFavorite =
                    false

                btnFavorite.setOnFavoriteChangeListener { buttonView, favorite ->
                    if (!favorite) {
                        favoriteTvShowDbHelper.deleteFavorite(tvShow.id!!)
                        btnFavorite.isFavorite = false
                    } else {
                        saveFavoriteTvShow(
                            tvShow.name,
                            tvShow.firstAirDate,
                            tvShow.voteCount,
                            tvShow.voteAverage,
                            tvShow.posterPath,
                            tvShow.overview,
                            tvShow.id
                        )
                        btnFavorite.isFavorite = true
                    }
                }
            }
        }
    }

    private fun isFavoriteMovieExist(myMovie: Movie): Boolean {
        movies = favoriteMovieDbHelper.getAllFavorite()
        movies.forEach {
            if (it.id.equals(myMovie.id)) {
                Log.d("TES123", "TRUE")
                return true
            }
        }
        Log.d("TES123", "FALSE")
        return false
    }

    private fun isFavoriteTvShowExists(myTvShow: TvShow): Boolean {
        tvShows = favoriteTvShowDbHelper.getAllFavorite()
        tvShows.forEach {
            if (it.id.equals(myTvShow.id)) {
                Log.d("TES123", "TRUE")
                return true
            }
        }
        Log.d("TES123", "FALSE")
        return false
    }

    private fun saveFavoriteMovie(
        title: String?,
        releaseDate: String?,
        voteCount: String?,
        voteAverage: String?,
        posterPath: String?,
        overview: String?,
        id: String?
    ) {
        favoriteMovie.title = title
        favoriteMovie.releaseDate = releaseDate
        favoriteMovie.voteCount = voteCount
        favoriteMovie.voteAverage = voteAverage
        favoriteMovie.posterPath = posterPath
        favoriteMovie.overview = overview
        favoriteMovie.id = id

        favoriteMovieDbHelper.addFavorite(favoriteMovie)
    }

    private fun saveFavoriteTvShow(
        title: String?,
        releaseDate: String?,
        voteCount: String?,
        voteAverage: String?,
        posterPath: String?,
        overview: String?,
        id: String?
    ) {
        favoriteTvShow.name = title
        favoriteTvShow.firstAirDate = releaseDate
        favoriteTvShow.voteCount = voteCount
        favoriteTvShow.voteAverage = voteAverage
        favoriteTvShow.posterPath = posterPath
        favoriteTvShow.overview = overview
        favoriteTvShow.id = id

        favoriteTvShowDbHelper.addFavorite(favoriteTvShow)
    }
}