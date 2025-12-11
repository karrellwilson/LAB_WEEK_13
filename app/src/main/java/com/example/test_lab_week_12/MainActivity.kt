package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Calendar // Penting: Import Calendar untuk filter tahun

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val movieAdapter = MovieAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("extra_movie", movie)
            startActivity(intent)
        }
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(movieRepository) as T
            }
        })[MovieViewModel::class.java]

        // --- UPDATE BAGIAN INI UNTUK FLOW & ASSIGNMENT ---
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Launch coroutine baru untuk observe movies
                launch {
                    movieViewModel.popularMovies.collect { movies ->

                        // --- BAGIAN ASSIGNMENT: Filter & Sort ---
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

                        val filteredMovies = movies
                            .filter { movie ->
                                // Hanya ambil film yang rilis tahun ini
                                movie.releaseDate?.startsWith(currentYear) == true
                            }
                            .sortedByDescending { movie ->
                                // Urutkan dari yang paling populer
                                movie.popularity
                            }

                        // Masukkan data yang SUDAH difilter ke adapter
                        movieAdapter.addMovies(filteredMovies)
                    }
                }

                // Launch coroutine baru untuk observe error
                launch {
                    movieViewModel.error.collect { errorMsg ->
                        if (errorMsg.isNotEmpty()) {
                            Snackbar.make(recyclerView, errorMsg, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}