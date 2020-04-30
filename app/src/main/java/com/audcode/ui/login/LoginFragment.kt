package com.ramadan.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.app
import com.audcode.ui.bottomNavigation
import com.audcode.ui.holderActivity
import com.audcode.ui.home.HomeVM
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.login.LoginVM
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    private lateinit var loginVM: LoginVM
    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newLoginComponent().inject(this)
        loginVM = ViewModelProvider(this, viewModelFactory)[LoginVM::class.java]
        bottomNavigation.visibility = View.VISIBLE
    }
    override fun getLayoutById() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBtn.setOnClickListener {
           holderActivity.onBackPressed()
        }

        signupText.setOnClickListener {
          holderActivity.loadFragment(RegisterFragment())
        }

    }

}