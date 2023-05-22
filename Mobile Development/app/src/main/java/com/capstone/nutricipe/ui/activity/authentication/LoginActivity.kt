package com.capstone.nutricipe.ui.activity.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.databinding.ActivityLoginBinding
import com.capstone.nutricipe.ui.activity.MainActivity
import com.capstone.nutricipe.ui.activity.dataStore
import com.capstone.nutricipe.ui.customview.button.ButtonLogin
import com.capstone.nutricipe.ui.customview.text.EmailEditText
import com.capstone.nutricipe.ui.customview.text.PasswordEditText
import com.capstone.nutricipe.ui.viewmodel.LoginViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: ButtonLogin
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText

    private lateinit var loginViewModel: LoginViewModel

    private var correctEmail = false
    private var correctPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginButton = binding.loginButton
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword

        // Get the user session using data store.
        val pref = Session.getInstance(dataStore)

        // Initialize the LoginViewModel
        loginViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) { token: String ->
            if (token.isNotEmpty()) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else if (token.isEmpty()) {
                loginButton.setOnClickListener {
                    login()
                }
            }
        }

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            emailEditText.setText(intent.getStringExtra("email"))
            correctEmail = true
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            passwordEditText.setText(intent.getStringExtra("password"))
            correctPassword = true
        }

        emailEditText.addTextChangedListener { text ->
            correctEmail = !text.isNullOrEmpty() && emailRegex.matches(text.toString())
            setLoginButtonEnable()
        }

        passwordEditText.addTextChangedListener { text ->
            correctPassword = !text.isNullOrEmpty() && text.length >= 8
            setLoginButtonEnable()
        }

        loginViewModel.message.observe(this) { message ->
            val toastMessage = when (message) {
                "Invalid Password" -> message
                "Email Not Found" -> message
                else -> message
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.acceptance.observe(this) {
            if (it) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
        

        binding.tvRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        loginViewModel.login(email, password)
    }

    private fun setLoginButtonEnable() {
        loginButton.isEnabled = correctEmail && correctPassword
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 100L
        private const val ALPHA_FULL = 1.0f
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    }
}