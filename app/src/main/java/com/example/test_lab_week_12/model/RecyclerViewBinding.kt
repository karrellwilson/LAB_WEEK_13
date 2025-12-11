package com.example.test_lab_week_12

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

// Binding Adapter custom bernama "list"
@BindingAdapter("list")
fun bindMovies(view: RecyclerView, movies: List<Movie>?) {
    val adapter = view.adapter as? MovieAdapter
    adapter?.addMovies(movies ?: emptyList())
}