package com.kt.tv_test.navigation

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kt.tv_test.R

class NavHostFragmentEx : NavHostFragment() {
    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider.addNavigator(
            ATVFragmentNavigator(requireContext(), childFragmentManager, R.id.fragment_container)
        )
    }
}