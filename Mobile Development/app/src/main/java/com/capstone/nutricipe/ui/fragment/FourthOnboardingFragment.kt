package com.capstone.nutricipe.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.FragmentFourthOnboardingBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.activity.intro.SplashActivity.Companion.PREF_IS_STARTED
import com.capstone.nutricipe.ui.utils.SharedPreference

class FourthOnboardingFragment : Fragment(R.layout.fragment_fourth_onboarding) {

    private var _binding: FragmentFourthOnboardingBinding? = null
    private val binding get() = _binding!!

    private val sharedPref: SharedPreference by lazy {
        SharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthOnboardingBinding.inflate(inflater, container, false)

        binding.btnGetStarted.setOnClickListener {
            sharedPref.put(PREF_IS_STARTED, true)
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

}