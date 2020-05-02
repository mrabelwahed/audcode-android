package com.ramadan.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audcode.AppConst.Keys.USER_EMAIL
import com.audcode.AppConst.Keys.USER_KEY
import com.audcode.AppConst.Keys.USER_MODEL
import com.audcode.AppConst.Keys.USER_PASSWORD
import com.audcode.R
import com.audcode.data.exceptions.Failure
import com.audcode.ui.*
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.login.RegisterVM
import com.audcode.ui.login.model.UserModel
import com.audcode.ui.profile.ProfileFragment
import com.audcode.ui.splash.MainActivity.Companion.PREF_NAME
import com.audcode.ui.splash.MainActivity.Companion.PRIVATE_MODE
import com.audcode.ui.viewstate.ServerDataState
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {

    override fun getLayoutById() = R.layout.fragment_register

    private lateinit var registerVM: RegisterVM
    private lateinit var email: String;
    private lateinit var password: String;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newRegisterComponent().inject(this)
        registerVM = ViewModelProvider(this, viewModelFactory)[RegisterVM::class.java]
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInText.setOnClickListener {
            holderActivity.onBackPressed()
        }

        createAccountFab.setOnClickListener {
            email = emailInput.text.toString()
            password = passwordInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty())
                registerVM.createUser(UserDTO(email, password))
        }

        registerVM.userViewState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ServerDataState.Success<*> -> {
                    val userModel = (it.item as UserModel)
                    saveUserModel(userModel)
                    handleUISuccess()
                    openProfileFragment(userModel, email, password)
                }

                is ServerDataState.Error -> {
                    handleUIError()
                    setError(it.failure)
                }

                is ServerDataState.Loading -> {
                    handleUILoading()
                }
            }
        })
    }



    private fun handleUISuccess() {
        Toast.makeText(activity, "done", Toast.LENGTH_LONG).show()
    }

    private fun openProfileFragment(userModel: UserModel, email: String, password: String) {
        val bundle = Bundle()
        bundle.putParcelable(USER_KEY, userModel)
        bundle.putString(USER_EMAIL, email)
        bundle.putString(USER_PASSWORD, password)
        val profileFragment = ProfileFragment()
        profileFragment.arguments = bundle
        holderActivity.loadFragment(profileFragment)
    }

    private fun setError(failure: Failure?) {

    }

    private fun handleUIError() {
    }

    private fun handleUILoading() {
    }

    override fun onResume() {
        super.onResume()
        toolBar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        bottomPlayer.visibility = View.GONE
    }
}