package com.kt.tv_test.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.kt.tv_test.R
import com.kt.tv_test.navigation.NavigationComponentManager

class PlaybackFragment : BaseFragment() {
    override var label = "fragment_playback"
    var isAddedNewPlayback = false
    var listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        Log.d("PlaybackFragment", "addOnDestinationChangedListener $destination")
        if (destination.label == "fragment_playback" && NavigationComponentManager.getPlaybackCnt() > 1) {
            isAddedNewPlayback = true
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playback, container, false)
    }

    override fun onSceneResume() {
        super.onSceneResume()
        if (isAddedNewPlayback) {
            findNavController().navigateUp()
        }
    }

    override fun onScenePause() {
        super.onScenePause()
        findNavController().removeOnDestinationChangedListener(listener)
        findNavController().addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener(listener)
        NavigationComponentManager.removePlayback()
    }
}