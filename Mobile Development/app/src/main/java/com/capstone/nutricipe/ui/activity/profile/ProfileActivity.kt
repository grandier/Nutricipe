package com.capstone.nutricipe.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.viewmodel.ProfileViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutricipe.ui.activity.dataStore


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Session.getInstance(dataStore)

        profileViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[ProfileViewModel::class.java]

        //Uji coba doang
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.logout.setOnClickListener {
            logout();
        }

        profileViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                profileViewModel.getProfile(token)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        profileViewModel.profile.observe(this) { profile ->
            if (profile != null) {
                binding.tvEmail.text = profile.email
                binding.tvName.text = profile.name
            }
        }
    }

    fun logout() {
        profileViewModel.logout()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}