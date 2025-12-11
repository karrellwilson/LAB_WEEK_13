package com.example.test_lab_week_12

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MovieApplication : Application() {
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        // Setup Retrofit & Database (Kode lama)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val movieService = retrofit.create(MovieService::class.java)
        val movieDatabase = MovieDatabase.getInstance(applicationContext)
        movieRepository = MovieRepository(movieService, movieDatabase)

        // --- KODE BARU WORK MANAGER ---

        // 1. Buat Constraint: Hanya jalan kalau ada internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 2. Buat Request: Jalan setiap 1 jam
        val workRequest = PeriodicWorkRequest.Builder(
            MovieWorker::class.java,
            1, TimeUnit.HOURS
        ).setConstraints(constraints)
            .addTag("movie-work")
            .build()

        // 3. Jadwalkan
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}