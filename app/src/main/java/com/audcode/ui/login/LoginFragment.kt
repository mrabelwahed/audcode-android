package com.ramadan.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.GifAdapterViewTypes
import com.audcode.ui.decoration.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_gif_list.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_login

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBtn.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_RegisterFragment)
        }

        signupText.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_homeFragment)
        }

    }
}