package com.dicoding.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.adapter.StoryAdapter
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.viewmodel.MainViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactoryMain

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var token = ""
    private var addClicked = false
    private val mainViewModel: MainViewModel by viewModels { ViewModelFactoryMain(this) }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }

        loadData()

        binding.btnAdd.setOnClickListener {
            addClicked = true
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun loadData() {
        mainViewModel.getSession().observe(this) {
            token = it.token
            if (!it.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.getStory(token).observe(this) {
                    storyAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (addClicked) {
            loadData()
            addClicked = false
            binding.rvStory.scrollToPosition(0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                mainViewModel.logout()
                true
            }
            R.id.btn_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}