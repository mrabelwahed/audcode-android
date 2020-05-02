package com.audcode.ui.profile

import android.os.Bundle
import android.view.View
import com.audcode.AppConst.Keys.USER_EMAIL
import com.audcode.AppConst.Keys.USER_KEY
import com.audcode.AppConst.Keys.USER_PASSWORD
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.holderActivity
import com.audcode.ui.login.model.UserModel
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

        getUserModel()?.let {
            userModel ->
            userEmailTextView.text = userModel.email
        }


    }

    private fun isAuthorized(): Boolean {
        getUserModel()?.let { userModel ->
            if (!userModel.authToken.isNullOrEmpty())
                return true
        }
        return false
    }

}