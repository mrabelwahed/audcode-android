package com.audcode.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audcode.R
import com.audcode.ui.home.HomeVM
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.audcode.ui.viewmodel.ViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.view_bottom_player.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    lateinit var homeVM: HomeVM

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutById(), container, false)
    }

    abstract fun getLayoutById(): Int

    fun observeLastPlayingEpisode() {
        homeVM.lastLiveEpisode.observe(viewLifecycleOwner, Observer { episode ->
            holderActivity.playingEpisode = episode
            //homeVM.setLastPlayedEpisode(episode)
            setLastPlayedEpisode(episode)
            //showLastPlayedEpisode()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newHomeComponent().inject(this)
        homeVM = ViewModelProvider(this, viewModelFactory)[HomeVM::class.java]
        bottomNavigation.visibility = View.VISIBLE
    }

    fun getLastPlayedEpisode(): EpisodeModel? {

        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(
            MainActivity.PREF_NAME,
            MainActivity.PRIVATE_MODE
        )
        val lastPlayedStr = sharedPref.getString(MainActivity.LAST_PLAYED, "")

        if (lastPlayedStr.isNotEmpty())
            return Gson().fromJson(lastPlayedStr, EpisodeModel::class.java)
        return null
    }

    private fun setLastPlayedEpisode(episodeModel: EpisodeModel) {
        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(
            MainActivity.PREF_NAME,
            MainActivity.PRIVATE_MODE
        )
        val editor = sharedPref.edit()
        editor.putString(MainActivity.LAST_PLAYED, Gson().toJson(episodeModel))
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        holderActivity.playingEpisode?.let { setLastPlayedEpisode(it) }

    }

    override fun onResume() {
        super.onResume()
        showLastPlayedEpisode()
    }

    fun showLastPlayedEpisode() {
        var lastPlayedEpisode = getLastPlayedEpisode()
        lastPlayedEpisode?.let { episode ->
            holderActivity.lastPlayedLayout.visibility = View.VISIBLE
            holderActivity.lastPlayedEpisodeTitle.text = episode.name
            if (episode.isPlaying)
                holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_pause_32px)
            else
                holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_play_arrow_24px)
        }
    }

}