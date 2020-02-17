package com.gify.ui

import com.gify.R


class MainActivity : BaseActivity() {

    override fun getLayoutById() = R.layout.activity_main
    private val gifListFragment = GifListFragment()

    override fun initUI() {
        supportFragmentManager.beginTransaction().add(R.id.container, gifListFragment).commit()
    }
}
