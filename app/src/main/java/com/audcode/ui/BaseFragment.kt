package com.audcode.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.audcode.AppConst
import com.audcode.AppConst.Keys.EPISODES
import com.audcode.AppConst.Keys.USER_MODEL
import com.audcode.R
import com.audcode.audio.PlayerState
import com.audcode.ui.episode_details.EpisodeDetailsFragment
import com.audcode.ui.home.HomeFragment
import com.audcode.ui.home.HomeVM
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.login.model.UserModel
import com.audcode.ui.splash.MainActivity
import com.audcode.ui.viewmodel.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.view_bottom_player.*
import java.lang.reflect.Type
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    lateinit var homeVM: HomeVM

    private var inMemoryEpisodes = arrayListOf<EpisodeModel>()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       getSavedEpisodes()?.let { inMemoryEpisodes = it }
        return inflater.inflate(getLayoutById(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newHomeComponent().inject(this)
        homeVM = ViewModelProvider(this, viewModelFactory)[HomeVM::class.java]
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holderActivity.bottomPlayerButton.setOnClickListener {
            getLastPlayedEpisode()?.let { episode -> handlePlayerStatus(episode) }
        }

        holderActivity.audioService.playerStatusLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { playerStatus ->

                if (this is EpisodeDetailsFragment) {
                    if (playerStatus is PlayerState.Playing) {
                        setLastPlayedEpisode(playerStatus.episode)
                        handleNotificationAction(playerStatus.episode)
                        handleBottomPlayerFromNotification(playerStatus.episode)
                    } else if (playerStatus is PlayerState.Paused) {
                        setLastPlayedEpisode(playerStatus.episode)
                        handleNotificationAction(playerStatus.episode)
                        handleBottomPlayerFromNotification(playerStatus.episode)
                    }
                } else if (this is HomeFragment) {
                    if (playerStatus is PlayerState.Playing) {
                        setLastPlayedEpisode(playerStatus.episode)
                        handleBottomPlayerFromNotification(playerStatus.episode)

                    } else if (playerStatus is PlayerState.Paused) {
                        setLastPlayedEpisode(playerStatus.episode)
                        handleBottomPlayerFromNotification(playerStatus.episode)
                    }
                }

                Log.e("status", playerStatus.toString())
            })

        holderActivity.bottomSaveButton.setOnClickListener {
            getLastPlayedEpisode()?.let {
                if (it.isSaved){
                    holderActivity.bottomSaveButton.setImageResource(R.drawable.ic_turned_in_not_24px)
                    it.isSaved = false
                    removeEpisode(it)
                }else{
                    it.isSaved = true
                    holderActivity.bottomSaveButton.setImageResource(R.drawable.ic_bookmarks_24px)
                    addEpisode(it)
                }
                setLastPlayedEpisode(it)
            }

        }
    }


    private fun handleBottomPlayerFromNotification(episode: EpisodeModel) {
        if (episode.isPlaying)
            holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_pause_32px)
        else
            holderActivity.bottomPlayerButton.setImageResource((R.drawable.ic_play_arrow_24px))
    }


    private fun handlePlayerStatus(episode: EpisodeModel) {
        if (episode.isPlaying) {
            holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_play_arrow_24px)
            holderActivity.pause(episode)
            episode.isPlaying = false
            setLastPlayedEpisode(episode)
        } else {
            holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_pause_32px)
            episode.isPlaying = true
            setLastPlayedEpisode(episode)
            holderActivity.play(episode)

        }
        homeVM.setLastPlayedEpisode(episode)
    }


    abstract fun getLayoutById(): Int

    fun observeLastPlayingEpisode() {
        homeVM.lastLiveEpisode.observe(viewLifecycleOwner, Observer { episode ->
            holderActivity.playingEpisode = episode
            setLastPlayedEpisode(episode)
        })
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

    fun setLastPlayedEpisode(episodeModel: EpisodeModel) {
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
        holderActivity.playingEpisode?.let {
            val episode = findEpisodeById(it.id)
            it.isSaved = episode.isSaved
            setLastPlayedEpisode(it)
        }
        saveEpisodes(inMemoryEpisodes)
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

    fun saveUserModel(userModel: UserModel) {
        val gson = Gson()
        val modelStr = gson.toJson(userModel)
        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(
            MainActivity.PREF_NAME,
            MainActivity.PRIVATE_MODE
        )
        sharedPref.edit().putString(AppConst.Keys.USER_MODEL, modelStr).commit()
    }

    fun getUserModel(): UserModel? {
        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(
            MainActivity.PREF_NAME,
            MainActivity.PRIVATE_MODE
        )
        val modelStr = sharedPref.getString(USER_MODEL, null)
        if (modelStr.isNullOrEmpty())
            return null
        return Gson().fromJson(modelStr, UserModel::class.java)
    }


    fun saveEpisodes(episodes: ArrayList<EpisodeModel>) {
            removeKeyFromSharedPref(EPISODES)
            val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(MainActivity.PREF_NAME, MainActivity.PRIVATE_MODE)
            val episodesStr = Gson().toJson(episodes)
            sharedPref.edit().putString(EPISODES, episodesStr).commit()
    }

    fun getSavedEpisodes(): ArrayList<EpisodeModel>? {
        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(MainActivity.PREF_NAME, MainActivity.PRIVATE_MODE)
        val episodesStr = sharedPref.getString(EPISODES, null)
        if (episodesStr.isNullOrEmpty())
            return null
        val type: Type = object : TypeToken<List<EpisodeModel?>?>() {}.type
        return Gson().fromJson(episodesStr, type)
    }

//    fun removeEpisode(episode: EpisodeModel){
//        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(MainActivity.PREF_NAME, MainActivity.PRIVATE_MODE)
//        var savedEpisodes = mutableListOf<EpisodeModel>()
//        getSavedEpisodes()?.let {
//            savedEpisodes = it
//        }
//        savedEpisodes.remove(episode)
//        val gson = Gson()
//        val episodesStr = gson.toJson(savedEpisodes)
//        sharedPref.edit().putString(EPISODES, episodesStr).commit()
//    }

    fun addEpisode(episodeModel: EpisodeModel){
        inMemoryEpisodes.add(episodeModel)
    }

    fun removeEpisode(episodeModel: EpisodeModel){
        val ite = inMemoryEpisodes.iterator()
        while(ite.hasNext())
            if(ite.next().id == episodeModel.id)
                ite.remove()

    }
    fun findEpisodeById(id:String) : EpisodeModel{
        var index =0
        for(item in inMemoryEpisodes)
            if (item.id == id){
                index =  inMemoryEpisodes.indexOf(item)
                break
            }

        return inMemoryEpisodes[index]
    }



    fun loadSavedEpisodes() = inMemoryEpisodes

    fun removeKeyFromSharedPref(key :String){
        val sharedPref: SharedPreferences = holderActivity.getSharedPreferences(MainActivity.PREF_NAME, MainActivity.PRIVATE_MODE)
        sharedPref.edit().remove(key).commit()
    }
}