package com.example.test_lab_week_12

import android.util.Log // Import Log
import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.database.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    private val apiKey = "b48ae55b88d6306db9fef196fe6ff9c9"

    // Fungsi 1: Digunakan oleh ViewModel (Flow) untuk menampilkan data ke UI
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // 1. Cek data di database lokal
            val movieDao = movieDatabase.movieDao()
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                // 2. Kalau kosong, ambil dari API
                val movies = movieService.getPopularMovies(apiKey).results

                // 3. Simpan ke database
                movieDao.addMovies(movies)

                // 4. Kirim data dari API
                emit(movies)
            } else {
                // 5. Kalau ada di database, kirim data lokal
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }

    // --- TAMBAHAN UNTUK WORK MANAGER (PART 3) ---
    // Fungsi 2: Digunakan oleh MovieWorker untuk update data di background
    suspend fun fetchMoviesFromNetwork() {
        val movieDao = movieDatabase.movieDao()
        try {
            // 1. Ambil data terbaru dari API
            val popularMovies = movieService.getPopularMovies(apiKey)
            val moviesFetched = popularMovies.results

            // 2. Masukkan ke database (data lama akan tertimpa karena OnConflictStrategy.REPLACE)
            movieDao.addMovies(moviesFetched)

            Log.d("MovieRepository", "Berhasil update database dari background worker")
        } catch (exception: Exception) {
            Log.d("MovieRepository", "An error occurred: ${exception.message}")
        }
    }
}