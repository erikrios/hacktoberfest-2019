package com.erikriosetiawan.cinemamovies.model

import com.google.gson.annotations.SerializedName

data class TvShowResponse(
    @SerializedName("results")
    val results: List<TvShow>?
)