package com.audcode.ui.profile

import android.os.Bundle
import android.view.View
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.holderActivity
import com.ramadan.login.LoginFragment

class ProfileFragment  : BaseFragment(){
    override fun getLayoutById() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isAuthorized()){
          holderActivity.loadFragment(LoginFragment())
        }
    }

    private fun isAuthorized() : Boolean{
        return false
    }

}