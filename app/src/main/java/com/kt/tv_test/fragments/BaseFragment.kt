package com.kt.tv_test.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.kt.tv_test.navigation.NavigationComponentManager

abstract class BaseFragment : Fragment() {
    val TAG = this::class.simpleName
    abstract val label: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState).run { Log.d(TAG, "onCreate($savedInstanceState)") }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context).run { Log.d(TAG, "onAttach()") }
    }

    override fun onStart() {
        super.onStart().run { Log.d(TAG, "onStart()") }
    }

    override fun onResume() {
        super.onResume().run { Log.d(TAG, "onResume()") }
    }

    override fun onPause() {
        super.onPause().run { Log.d(TAG, "onPause()") }
    }

    override fun onStop() {
        super.onStop().run { Log.d(TAG, "onStop()") }
    }

    override fun onDetach() {
        super.onDetach().run { Log.d(TAG, "onDetach()") }
    }

    override fun onDestroyView() {
        super.onDestroyView().run { Log.d(TAG, "onDestroyView()") }
    }

    override fun onDestroy() {
        super.onDestroy().run { Log.d(TAG, "onDestroy()") }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "onHiddenChanged hidden $hidden")
        Log.d(TAG, "onHiddenChanged isHidden $isHidden")
        Log.d(TAG, "onHiddenChanged isRemoving $isRemoving")
        Log.d(TAG, "onHiddenChanged isVisible $isVisible")
        if (isRemoving) {
            onScenePause()
        }
        else {
            if (hidden && isHidden) {
                onScenePause()
            }
            else {
                Log.d(TAG, "onHiddenChanged label $label")
                Log.d(TAG, "onHiddenChanged label current ${NavigationComponentManager.getCurrentDestination()?.label}")
                if (label == NavigationComponentManager.getCurrentDestination()?.label) {
                }
                onSceneResume()
            }
        }
    }

    open fun onSceneResume() {
        Log.d(TAG, "onSceneResume")
    }

    open fun onScenePause() {
        Log.d(TAG, "onScenePause")
    }
}