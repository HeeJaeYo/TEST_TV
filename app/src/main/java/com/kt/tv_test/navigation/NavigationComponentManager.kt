package com.kt.tv_test.navigation

import android.util.Log
import androidx.navigation.NavDestination

object NavigationComponentManager {
    private var currentDestination: NavDestination? = null
    private var playbackCnt = 0
    fun setCurrentDestination(destination: NavDestination) {
        Log.d("NavigationComponentManager", "setCurrentDestination() $destination")
        currentDestination = destination
    }

    fun getCurrentDestination(): NavDestination? = currentDestination

    fun addPlayback() {
        playbackCnt++
    }

    fun removePlayback() {
        if (playbackCnt > 0) playbackCnt--
    }

    fun getPlaybackCnt() = playbackCnt
}