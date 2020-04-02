package com.audcode.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

abstract  class BaseActivity : AppCompatActivity() {
    abstract  fun getLayoutById(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutById())
        initUI()
    }
//
//    private fun initPlayer() {
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(activity)
//
//        dataSourceFactory = DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "audcode"))
//
//        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("https://audcode-space.fra1.digitaloceanspaces.com/eee.m4a"))
//    }


    abstract fun initUI()

}