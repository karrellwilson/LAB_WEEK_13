package com.example.test_lab_week_12

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable // <--- Tambahkan Import ini

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val popularity: Double
) : Serializable // <--- Tambahkan ini agar bisa dikirim lewat Intent