package com.audcode.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.bottomNavigation
import com.audcode.ui.bottomPlayerView
import com.audcode.ui.toolBar
import kotlinx.android.synthetic.main.main_toolbar.*

class SplashFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       toolBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        bottomPlayerView.visibility = View.GONE
        android.os.Handler().postDelayed(
            Runnable {
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_homeFragment)
            }
            , 1000
        )
    }

}