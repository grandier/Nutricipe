package com.capstone.nutricipe.ui.activity.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.databinding.ActivityRegisterBinding
import com.capstone.nutricipe.ui.activity.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Uji Coba Doang
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}