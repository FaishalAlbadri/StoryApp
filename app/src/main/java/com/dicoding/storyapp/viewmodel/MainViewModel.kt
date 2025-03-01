package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.ListStoryItem
import com.dicoding.storyapp.data.paging.StoryRepository
import com.dicoding.storyapp.data.user.UserData
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