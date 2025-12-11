package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager // Import Grid Layout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)

        // UBAH JADI GRIDLAYOUTMANAGER (2 KOLOM)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Handle Klik: Buka MovieDetailActivity
        val movieAdapter = MovieAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("extra_movie", movie) // Kirim object movie
            startActivity(intent)
        }

        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(movieRepository) as T
            }
        })[MovieViewModel::class.java]

        movieViewModel.popularMovies.observe(this) { popularMovies ->
            // Filter film tahun ini (Opsional, sesuai modul)
            // val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            movieAdapter.addMovies(popularMovies)
        }

        movieViewModel.error.observe(this) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Snackbar.make(recyclerView, errorMsg, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}