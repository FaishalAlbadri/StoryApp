package com.dicoding.storyapp.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.data.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem
        binding.apply {
            tvName.text = data.name
            tvStory.text = data.description
            Glide.with(this@DetailStoryActivity)
                .load(data.photo)
                .fitCenter()
                .into(ivImg)
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}