package com.example.test_lab_week_12

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val onClick: (Movie) -> Unit) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movies = mutableListOf<Movie>()

    fun addMovies(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies) // Ini aman karena kita kirim emptyList() jika null
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onClick(movie) }
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        // HAPUS reference overview karena di Grid Layout (item_movie.xml) sudah dihapus
        // private val overview: TextView = itemView.findViewById(R.id.movie_overview)
        private val poster: ImageView = itemView.findViewById(R.id.movie_poster)

        fun bind(movie: Movie) {
            title.text = movie.title

            // HAPUS pengisian overview
            // overview.text = movie.overview

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(poster)
        }
    }
}