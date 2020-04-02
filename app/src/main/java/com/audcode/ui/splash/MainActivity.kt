package com.audcode.ui.splash

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import com.audcode.R
import com.audcode.ui.BaseActivity
import com.audcode.ui.home.model.EpisodeModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.view_bottom_player.*

class MainActivity : BaseActivity() {
    var isFirstLaunch = true
    var playingEpisode: EpisodeModel? = null
    override fun getLayoutById() = R.layout.activity_main
    override fun initUI() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainNavHostFragment, SplashFragment()).commit()

    }

    fun getLastPlayedEpisode(): EpisodeModel? {
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val lastPlayedStr = sharedPref.getString(LAST_PLAYED, "")

        if (lastPlayedStr.isNotEmpty())
            return Gson().fromJson(lastPlayedStr, EpisodeModel::class.java)
        return null
    }

    fun setLastPlayedEpisode() {
        playingEpisode?.let {
            val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
            val editor = sharedPref.edit()
            editor.putString(LAST_PLAYED, Gson().toJson(it))
            editor.apply()
        }
    }

    override fun onPause() {
        super.onPause()
        setLastPlayedEpisode()
    }

    fun showLastPlayedEpisode() {
        var lastPlayedEpisode = getLastPlayedEpisode()
        lastPlayedEpisode?.let {
            lastPlayedLayout.visibility = View.VISIBLE
            lastPlayedLayout.findViewById<TextView>(R.id.lastPlayedEpisodeTitle).text = it.name
            //lastPlayedLayout.findViewById<ImageView>(R.id.image).setImageResource(lastPlayedEpisode.)
        }
    }


    companion object {
        const val SELECTED_EPISODE = "selected_episode"
        const val FIRST_LAUNCH = "first_launch"
        const val PRIVATE_MODE = 0
        const val PREF_NAME = "audcode"
        const val LAST_PLAYED = "last_played"
    }


}