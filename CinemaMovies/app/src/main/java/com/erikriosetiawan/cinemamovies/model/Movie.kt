package com.erikriosetiawan.cinemamovies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("popularity")
    var popularity: String?,

    @SerializedName("vote_count")
    var voteCount: String?,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("id")
    var id: String?,

    @SerializedName("title")
    var title: String?,

    @SerializedName("vote_average")
    var voteAverage: String?,

    @SerializedName("overview")
    var overview: String?,

    @SerializedName("release_date")
    var releaseDate: String?
) : Parcelable