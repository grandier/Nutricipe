package com.capstone.nutricipe.ui.activity.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.databinding.ActivityLoginBinding
import com.capstone.nutricipe.ui.activity.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Uji Coba Doang
        binding.loginButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}