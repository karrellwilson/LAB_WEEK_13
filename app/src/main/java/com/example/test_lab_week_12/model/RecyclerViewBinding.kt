package com.example.test_lab_week_12

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("list")
fun bindMovies(view: RecyclerView, movies: List<Movie>?) {
    val adapter = view.adapter as? MovieAdapter
    if (adapter != null) {
        adapter.addMovies(movies ?: emptyList())
    }
}