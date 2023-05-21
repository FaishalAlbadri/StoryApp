package com.dicoding.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.StoryResponse
import com.dicoding.storyapp.data.user.UserPreferences
import com.dicoding.storyapp.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _mapsResponse = MutableLiveData<StoryResponse>()
    val mapsResponse: LiveData<StoryResponse> = _mapsResponse

    fun getStoryMaps() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService(getToken()).getListStoriesWithLocation()
            client.enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _message.value = Event(response.body()?.message.toString())
                        _mapsResponse.value = response.body()
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
        }
    }

    suspend fun getToken(): String {
        return userPreferences.getSession().first().token
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }

}