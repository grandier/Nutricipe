package com.capstone.nutricipe.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.ActivityAddPhotoBinding

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Uji coba doang
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}