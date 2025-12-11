package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.database.MovieDatabase // Import database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// Tambahkan movieDatabase di constructor
class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {

    private val apiKey = "b48ae55b88d6306db9fef196fe6ff9c9"

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
}