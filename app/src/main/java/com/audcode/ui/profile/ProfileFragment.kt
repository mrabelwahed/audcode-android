package com.audcode.ui.profile

import android.os.Bundle
import android.view.View
import com.audcode.AppConst.Keys.USER_EMAIL
import com.audcode.AppConst.Keys.USER_PASSWORD
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.holderActivity
import com.audcode.ui.supportActionBar
import com.audcode.ui.toolBar
import com.ramadan.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    private var userEmail: String? = null
    private var userPassword: String? = null
    override fun getLayoutById() = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isAuthorized()) {
            arguments?.getString(USER_EMAIL)?.let { userEmail = it }
            arguments?.getString(USER_PASSWORD)?.let { userPassword = it }
            val bundle = Bundle()
            bundle.putString(USER_EMAIL, userEmail)
            bundle.putString(USER_PASSWORD, userPassword)
            val loginFragment = LoginFragment()
            loginFragment.arguments = bundle
            holderActivity.loadFragment(loginFragment)
        }
        toolBar.visibility = View.VISIBLE
        toolBar.title = getString(R.string.menu_profile)
        holderActivity.setSupportActionBar(toolBar)
        // add back arrow to toolbar
        if (holderActivity.supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

        getUserModel()?.let { userModel ->
           userFullName.text = userModel.fullName
        }


        signOutBtn.setOnClickListener {
            //TODO:POPUp
            getUserModel()?.let { userModel ->
                userModel.authToken = null
                saveUserModel(userModel)
                holderActivity.loadFragment(LoginFragment())
            }
        }

        toolBar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_ios_24px)
        toolBar.setNavigationOnClickListener(View.OnClickListener {
            // back button pressed
            holderActivity.onBackPressed()
        })
    }


    private fun isAuthorized(): Boolean {
        getUserModel()?.let { userModel ->
            if (!userModel.authToken.isNullOrEmpty())
                return true
        }
        return false
    }

}