package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "b48ae55b88d6306db9fef196fe6ff9c9" // API Key kamu

    // Perhatikan: Fungsi ini tidak 'suspend', tapi mengembalikan Flow
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // Emit (kirim) data dari API
            val response = movieService.getPopularMovies(apiKey)
            emit(response.results)
        }.flowOn(Dispatchers.IO) // Pastikan berjalan di Background Thread
    }
}