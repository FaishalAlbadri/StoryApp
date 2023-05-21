package com.dicoding.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.dicoding.storyapp.data.ListStoryItem
import com.dicoding.storyapp.data.paging.StoryRepository
import com.dicoding.storyapp.data.user.UserData
import com.dicoding.storyapp.di.StoryInjection
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun getSession(): LiveData<UserData> {
        return storyRepository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}

class ViewModelFactoryMain(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(StoryInjection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}