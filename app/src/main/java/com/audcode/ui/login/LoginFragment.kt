package com.ramadan.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audcode.AppConst.Keys.USER_EMAIL
import com.audcode.AppConst.Keys.USER_PASSWORD
import com.audcode.R
import com.audcode.ui.*
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.login.LoginVM
import com.audcode.ui.login.model.UserModel
import com.audcode.ui.profile.ProfileFragment
import com.audcode.ui.viewstate.ServerDataState
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    private var userEmail: String? = null
    private var userPassword: String? = null
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

        arguments?.getString(USER_EMAIL)?.let { userEmail = it }
        arguments?.getString(USER_PASSWORD)?.let { userPassword = it }

        userEmail?.let { emailInput.setText(userEmail) }
        userPassword?.let { passwordInput.setText(userPassword) }


        loginBtn.setOnClickListener {
            userEmail = emailInput.text.toString()
            userPassword = passwordInput.text.toString()
            userEmail?.let { email ->
                userPassword?.let { pass ->
                    if (email.isNotEmpty() && pass.isNotEmpty())
                        loginVM.authenticateUser(UserDTO(email, pass))
                }
            }
        }

        loginVM.userViewState.observe(viewLifecycleOwner , Observer{
            when (it) {
                is ServerDataState.Success<*> -> {
                    val authToken = it.item as String
                    getUserModel()?.let {
                        userModel ->  userModel.authToken = authToken
                        saveUserModel(userModel)
                    }
                    handleUISuccess()
                    openProfileFragment()
                }

                is ServerDataState.Error -> {
//                    handleUIError()
//                    setError(it.failure)
                }

                is ServerDataState.Loading -> {
//                    handleUILoading()
                }
            }
            //save token and set user as authorized
//            getUserModel()?.let {
//                user ->
//                user?.authToken = token
//            }
        })

        signupText.setOnClickListener {
            holderActivity.loadFragment(RegisterFragment())
        }

    }

    private fun openProfileFragment() {
        holderActivity.loadFragment(ProfileFragment())
    }

    private fun handleUISuccess() {
    }

    override fun onResume() {
        super.onResume()
        toolBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        bottomPlayer.visibility = View.GONE
    }

}