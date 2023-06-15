package com.capstone.nutricipe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.FragmentThirdOnboardingBinding

class ThirdOnboardingFragment : Fragment(R.layout.fragment_third_onboarding) {

    private var _binding: FragmentThirdOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

}