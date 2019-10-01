package com.erikriosetiawan.cinemamovies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    @SerializedName("popularity")
    var popularity: String?,

    @SerializedName("vote_count")
    var voteCount: String?,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("id")
    var id: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("vote_average")
    var voteAverage: String?,

    @SerializedName("overview")
    var overview: String?,

    @SerializedName("first_air_date")
    var firstAirDate: String?
) : Parcelable