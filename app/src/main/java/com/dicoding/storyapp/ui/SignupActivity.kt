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
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.viewmodel.SignupViewModel

class SignupActivity : BaseNoActionBarActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel>()
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAnimation()
        createLoading()

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        signupViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.signupResponse.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnSignup.setOnClickListener {
            if (binding.edRegisterName.length() == 0 || binding.edRegisterEmail.length() == 0 && binding.edRegisterPassword.length() < 8) {
                binding.edRegisterName.error = getString(R.string.required_field)
                binding.edRegisterEmail.error = getString(R.string.required_field)
                binding.edRegisterPassword.error = getString(R.string.required_field)
            } else {
                signupViewModel.signup(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                )
            }
        }
    }

    private fun setAnimation() {
        val title = ObjectAnimator.ofFloat(binding.txtTitle, View.ALPHA, 1f).setDuration(400)
        val username = ObjectAnimator.ofFloat(binding.tlUsername, View.ALPHA, 1f).setDuration(400)
        val email = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(400)
        val button = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, username, email, password, button)
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
}