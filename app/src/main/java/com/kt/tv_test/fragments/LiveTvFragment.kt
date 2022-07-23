package com.kt.tv_test.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.tv_test.R

class LiveTvFragment : BaseFragment() {
    override var label = "fragment_live_tv"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_live_tv, container, false)
    }

    override fun onSceneResume() {
        super.onSceneResume()
        Log.d(TAG, "arguments actionHome ${arguments?.getBoolean("actionHome", false)}")
    }
}