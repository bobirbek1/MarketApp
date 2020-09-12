package com.idrok.marketapp.splashUi.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idrok.marketapp.R


class SplashFragment : Fragment() {

    private lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_splash, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
    }
}