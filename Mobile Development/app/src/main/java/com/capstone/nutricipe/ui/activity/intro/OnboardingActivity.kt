package com.capstone.nutricipe.ui.activity.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.nutricipe.databinding.ActivityOnboardingBinding
import com.capstone.nutricipe.ui.utils.ViewPagerAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpOnboarding.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.ipOnboarding.attachTo(binding.vpOnboarding)
    }
}