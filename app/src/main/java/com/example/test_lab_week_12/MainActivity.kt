package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.test_lab_week_12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Inisialisasi binding object
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ganti setContentView biasa dengan DataBindingUtil
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Setup RecyclerView & Adapter
        binding.movieList.layoutManager = GridLayoutManager(this, 2)

        val movieAdapter = MovieAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("extra_movie", movie)
            startActivity(intent)
        }
        binding.movieList.adapter = movieAdapter

        // Setup ViewModel
        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(movieRepository) as T
            }
        })[MovieViewModel::class.java]

        // --- BINDING DATA ---
        // Hubungkan layout variable 'viewModel' dengan instance movieViewModel kita
        binding.viewModel = movieViewModel

        // Set lifecycle owner agar binding bisa mengamati LiveData/StateFlow
        binding.lifecycleOwner = this

        // KITA SUDAH TIDAK BUTUH lifecycleScope.launch DI SINI!
        // DataBinding yang akan otomatis mengupdate RecyclerView lewat RecyclerViewBinding.kt
    }
}