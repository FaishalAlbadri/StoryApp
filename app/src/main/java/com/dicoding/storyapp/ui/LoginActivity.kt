package com.dicoding.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyapp.R
import com.dicoding.storyapp.base.BaseNoActionBarActivity
import com.dicoding.storyapp.data.user.UserData
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.viewmodel.LoginViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory

class LoginActivity : BaseNoActionBarActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)
        setAnimation()
        createLoading()

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.loginResponse.observe(this) {
            loginViewModel.saveSession(
                UserData(
                    it.loginResult?.userId.toString(),
                    it.loginResult?.name.toString(),
                    AUTH_KEY + (it.loginResult?.token.toString()),
                    true
                )
            )
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.edLoginEmail.length() == 0 || binding.edLoginPassword.length() < 8) {
                binding.edLoginEmail.error = getString(R.string.required_field)
                binding.edLoginPassword.error = getString(R.string.required_field)
            } else {
                loginViewModel.login(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString()
                )
            }
        }
    }


    private fun setAnimation() {
        val title = ObjectAnimator.ofFloat(binding.txtTitle, View.ALPHA, 1f).setDuration(400)
        val message = ObjectAnimator.ofFloat(binding.txtMessage, View.ALPHA, 1f).setDuration(400)
        val email = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(400)
        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, message, email, password, button)
            startDelay = 400
        }.start()
    }

    private fun createLoading() {
        loading = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.layout_loading_dialog)
            .create()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }
}