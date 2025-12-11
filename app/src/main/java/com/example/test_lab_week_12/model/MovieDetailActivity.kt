package com.example.test_lab_week_12

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Ambil data movie yang dikirim
        val movie = intent.getSerializableExtra("extra_movie") as? Movie

        if (movie != null) {
            val title: TextView = findViewById(R.id.detail_title)
            val overview: TextView = findViewById(R.id.detail_overview)
            val releaseDate: TextView = findViewById(R.id.detail_release_date)
            val poster: ImageView = findViewById(R.id.detail_poster)

            title.text = movie.title
            overview.text = movie.overview
            releaseDate.text = movie.releaseDate

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(poster)
        }
    }
}