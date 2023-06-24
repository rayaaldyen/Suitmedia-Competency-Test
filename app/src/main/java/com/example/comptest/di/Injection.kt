package com.example.comptest.di

import android.content.Context
import com.example.comptest.database.UserDatabase
import com.example.comptest.model.api.ApiConfig
import com.example.comptest.model.data.UsersRepository

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val database = UserDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return UsersRepository(apiService, database)
    }
}