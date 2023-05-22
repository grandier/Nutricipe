package com.capstone.nutricipe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.capstone.nutricipe.R
import com.capstone.nutricipe.databinding.FragmentFirstOnboardingBinding

class FirstOnboardingFragment : Fragment(R.layout.fragment_first_onboarding) {

    private var _binding: FragmentFirstOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstOnboardingBinding.inflate(inflater, container, false)



        return binding.root
    }
}