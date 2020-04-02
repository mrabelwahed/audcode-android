package com.audcode.ui.splash

import android.os.Bundle
import android.view.View
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.bottomNavigation
import com.audcode.ui.bottomPlayer
import com.audcode.ui.home.HomeFragment
import com.audcode.ui.splash.MainActivity.Companion.FIRST_LAUNCH
import com.audcode.ui.toolBar
import kotlinx.android.synthetic.main.view_bottom_player.*

class SplashFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        bottomPlayer.visibility = View.GONE
        android.os.Handler().postDelayed(
            Runnable {
                (activity as MainActivity).
                    supportFragmentManager.beginTransaction().replace(R.id.mainNavHostFragment,HomeFragment())
                    .commit()
            }
            , 1000
        )
    }

}