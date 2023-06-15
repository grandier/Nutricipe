package com.capstone.nutricipe.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.capstone.nutricipe.ui.fragment.FirstOnboardingFragment
import com.capstone.nutricipe.ui.fragment.FourthOnboardingFragment
import com.capstone.nutricipe.ui.fragment.SecondOnboardingFragment
import com.capstone.nutricipe.ui.fragment.ThirdOnboardingFragment

@Suppress("DEPRECATION")
class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    private val fragment = listOf(
        FirstOnboardingFragment(),
        SecondOnboardingFragment(),
        ThirdOnboardingFragment(),
        FourthOnboardingFragment()
    )

    override fun getCount(): Int {
        return fragment.size
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }
}