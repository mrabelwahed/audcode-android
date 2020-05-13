package com.audcode.ui.splash

import android.os.Bundle
import android.view.View
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.bottomNavigation
import com.audcode.ui.bottomPlayer
import com.audcode.ui.home.HomeFragment
import com.audcode.ui.toolBar

class SplashFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        android.os.Handler().postDelayed(
            Runnable {
                val homeFragment = HomeFragment()
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commit()
            }
            , 1000
        )
    }

    override fun onResume() {
        super.onResume()
        toolBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        bottomPlayer.visibility = View.GONE
    }


}