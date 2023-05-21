package com.dicoding.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.storyapp.api.local.StoryDatabase
import com.dicoding.storyapp.data.paging.StoryRepository
import com.dicoding.storyapp.data.user.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        return StoryRepository.getInstance(
            UserPreferences.getInstance(context.dataStore),
            StoryDatabase.getDatabase(context)
        )
    }
}