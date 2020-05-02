package com.audcode.ui.splash

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import androidx.annotation.NonNull
import com.audcode.AppConst
import com.audcode.AppConst.Keys.PAUSE
import com.audcode.AppConst.Keys.PLAY
import com.audcode.R
import com.audcode.audio.AudioService
import com.audcode.ui.BaseActivity
import com.audcode.ui.home.HomeFragment
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.library.LibraryFragment
import com.audcode.ui.profile.ProfileFragment
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


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
       bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SplashFragment()).commit()
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
            bundle.putParcelable(AppConst.Keys.SERVICE_EPISODE,episodeModel)
            bundle.putString(AppConst.Keys.SERVICE_ACTION,PLAY)
            intent.putExtra(AppConst.Keys.BUNDLE_KEY,bundle)
            Util.startForegroundService(this,intent)
        }
    }

    fun pause(episodeModel: EpisodeModel , withStop:Boolean = false){
        Intent(this, AudioService::class.java).also { intent ->
            val bundle = Bundle()
            bundle.putParcelable(AppConst.Keys.SERVICE_EPISODE,episodeModel)
            bundle.putString(AppConst.Keys.SERVICE_ACTION,PAUSE)
            intent.putExtra(AppConst.Keys.BUNDLE_KEY,bundle)
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


    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_home -> {
                         loadFragment(HomeFragment())
                        return true
                    }
                    R.id.action_lib -> {
                         loadFragment(LibraryFragment())
                        return true
                    }

                    R.id.action_profile -> {
                        loadFragment(ProfileFragment())
                        return true
                    }
                }
                return false
            }
        }


    companion object {
        const val SELECTED_EPISODE = "selected_episode"
        const val PRIVATE_MODE = 0
        const val PREF_NAME = "audcode"
        const val LAST_PLAYED = "last_played"
    }

    override fun onBackPressed() {
        val selectedItem = bottomNavigationView.selectedItemId
        if (R.id.action_home != selectedItem) {
            bottomNavigationView.selectedItemId = R.id.action_home
        } else {
           super.onBackPressed()
        }
    }



}