package com.gify.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.gify.R
import com.gify.ui.BaseFragment

class SplashFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        android.os.Handler().postDelayed(
            Runnable {
                NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_LoginFragment)
            }
            , 1000
        )
    }

}