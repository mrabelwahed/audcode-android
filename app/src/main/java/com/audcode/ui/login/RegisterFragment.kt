package com.ramadan.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audcode.R
import com.audcode.data.exceptions.Failure
import com.audcode.ui.BaseFragment
import com.audcode.ui.app
import com.audcode.ui.bottomNavigation
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.holderActivity
import com.audcode.ui.login.RegisterVM
import com.audcode.ui.login.model.UserModel
import com.audcode.ui.viewstate.ServerDataState
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {

    override fun getLayoutById() = R.layout.fragment_register

    private lateinit var registerVM: RegisterVM

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
            var email = emailInput.text.toString()
            var password = passwordInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty())
                registerVM.createUser(UserDTO(email, password))
        }

        registerVM.userViewState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ServerDataState.Success<*> -> {
                    val userModel = (it.item as UserModel)
                    handleUISuccess()
                    setData(userModel)
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
        Toast.makeText(activity,"done",Toast.LENGTH_LONG).show()
    }

    private fun setData(userModel: UserModel) {

    }

    private fun setError(failure: Failure?) {

    }

    private fun handleUIError() {
    }

    private fun handleUILoading() {
    }
}