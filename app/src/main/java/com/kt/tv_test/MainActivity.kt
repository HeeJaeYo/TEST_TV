package com.kt.tv_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kt.tv_test.fragments.LiveTvFragmentDirections
import com.kt.tv_test.navigation.NavigationComponentManager
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
    }
    val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            ?: throw IllegalStateException("the container MUST contain a fragment at least one")
        navHostFragment.findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d(TAG, "addOnDestinationChangedListener arguments $destination")
            Log.d(TAG, "addOnDestinationChangedListener arguments $arguments")
            navHostFragment.childFragmentManager?.fragments?.forEach {
                Log.d(TAG, "fragments $it")
                Log.d(TAG, "fragments ${it.tag}")
                Log.d(TAG, "destination ${destination.id}")
            }
            if (destination.navigatorName == "atv_fragment") {
                NavigationComponentManager.setCurrentDestination(destination)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_1 -> {
                Log.d(TAG, "clear all")
                navController.navigate(R.id.action_to_liveTv, bundleOf("clearAll" to true))
                true
            }
            KeyEvent.KEYCODE_2 -> {
                Log.d(TAG, "go to Home")
                navController.navigate(R.id.action_to_home)
                true
            }
            KeyEvent.KEYCODE_3 -> {
                Log.d(TAG, "go to clear Home")
                var backstackListener: FragmentManager.OnBackStackChangedListener by Delegates.notNull()
                backstackListener = FragmentManager.OnBackStackChangedListener {
                    Log.d(TAG, "OnBackStackChangedListener")
                    navHostFragment.childFragmentManager.removeOnBackStackChangedListener(backstackListener)
                    navController.navigate(R.id.action_to_home)
                }
                navHostFragment.childFragmentManager.addOnBackStackChangedListener(backstackListener)
                navController.navigate(R.id.action_to_home_clear, bundleOf("actionHome" to true))


                true
            }
            KeyEvent.KEYCODE_4 -> {
                Log.d(TAG, "go to detail")
                navController.navigate(R.id.action_to_detail)
                true
            }
            KeyEvent.KEYCODE_5 -> {
                navHostFragment.childFragmentManager.fragments?.forEach {
                    Log.d(TAG, "fragments $it")
                }
                NavigationComponentManager.addPlayback()
                navController.navigate(R.id.action_to_play_back)
                true
            }
            KeyEvent.KEYCODE_6 -> {
                navController.navigate(R.id.action_to_header)
                true
            }
            KeyEvent.KEYCODE_7 -> {
                true
            }
            KeyEvent.KEYCODE_8 -> {
                true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                navHostFragment.childFragmentManager.fragments?.forEach {
                    Log.d(TAG, "fragments $it")
                }
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                navController.navigateUp()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}