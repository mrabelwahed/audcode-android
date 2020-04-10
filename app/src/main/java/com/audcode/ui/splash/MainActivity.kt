package com.audcode.ui.splash

import android.content.*
import android.os.Bundle
import android.os.IBinder
import com.audcode.AppConst
import com.audcode.AppConst.keys.PAUSE
import com.audcode.AppConst.keys.PLAY
import com.audcode.R
import com.audcode.audio.AudioService
import com.audcode.ui.BaseActivity
import com.audcode.ui.home.model.EpisodeModel
import com.google.android.exoplayer2.util.Util

class MainActivity : BaseActivity() {

    var playingEpisode: EpisodeModel? = null
    private var mBound: Boolean = false
     lateinit var audioService :AudioService

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as AudioService.AudioServiceBinder
            audioService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun getLayoutById() = R.layout.activity_main


    override fun initUI() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainNavHostFragment, SplashFragment()).commit()
    }





    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, AudioService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }



    fun play(episodeModel: EpisodeModel){
        Intent(this, AudioService::class.java).also { intent ->
            val bundle = Bundle()
            bundle.putParcelable(AppConst.keys.SERVICE_EPISODE,episodeModel)
            bundle.putString(AppConst.keys.SERVICE_ACTION,PLAY)
            intent.putExtra(AppConst.keys.BUNDLE_KEY,bundle)
            Util.startForegroundService(this,intent)
        }
    }

    fun pause(episodeModel: EpisodeModel , withStop:Boolean = false){
        Intent(this, AudioService::class.java).also { intent ->
            val bundle = Bundle()
            bundle.putParcelable(AppConst.keys.SERVICE_EPISODE,episodeModel)
            bundle.putString(AppConst.keys.SERVICE_ACTION,PAUSE)
            intent.putExtra(AppConst.keys.BUNDLE_KEY,bundle)
            Util.startForegroundService(this,intent)
        }
        if (withStop)
            stopAudioService()
    }

    private fun stopAudioService() {
        unbindService(connection)
        mBound = false
        stopService(Intent(this, AudioService::class.java))
    }


    companion object {
        const val SELECTED_EPISODE = "selected_episode"
        const val PRIVATE_MODE = 0
        const val PREF_NAME = "audcode"
        const val LAST_PLAYED = "last_played"
    }


}