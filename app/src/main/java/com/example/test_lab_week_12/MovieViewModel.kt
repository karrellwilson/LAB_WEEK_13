package com.example.test_lab_week_12

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // Internal StateFlow (untuk menampung data dari Flow Repository)
    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _error = MutableStateFlow("")

    // --- PERUBAHAN PENTING DI SINI ---
    // Kita ubah menjadi LiveData agar Data Binding di XML bisa membacanya tanpa error
    val popularMovies: LiveData<List<Movie>> = _popularMovies.asLiveData()
    val error: LiveData<String> = _error.asLiveData()
    // ---------------------------------

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .catch { exception ->
                    _error.value = "An exception occurred: ${exception.message}"
                }
                .collect { movies ->
                    _popularMovies.value = movies
                }
        }
    }
}