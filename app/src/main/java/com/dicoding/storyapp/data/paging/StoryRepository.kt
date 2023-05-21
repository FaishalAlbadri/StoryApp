package com.dicoding.storyapp.data.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.local.StoryDatabase
import com.dicoding.storyapp.data.ListStoryItem
import com.dicoding.storyapp.data.user.UserData
import com.dicoding.storyapp.data.user.UserPreferences

class StoryRepository(
    private val pref: UserPreferences,
    private val database: StoryDatabase
) {
    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(
                database,
                ApiConfig.getApiService(token)
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getSession(): LiveData<UserData> {
        return pref.getSession().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            pref: UserPreferences,
            database: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(pref, database)
            }.also { instance = it }
    }
}