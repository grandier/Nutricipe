package com.capstone.nutricipe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.FragmentSecondOnboardingBinding

class SecondOnboardingFragment : Fragment(R.layout.fragment_second_onboarding) {

    private var _binding: FragmentSecondOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

}