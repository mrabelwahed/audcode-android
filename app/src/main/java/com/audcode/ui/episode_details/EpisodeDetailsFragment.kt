package com.audcode.ui.episode_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.audcode.R
import com.audcode.ui.*
import com.audcode.ui.dialog.AlertDialogFragment
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.audcode.ui.splash.MainActivity.Companion.SELECTED_EPISODE
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_episode_details.*
import kotlinx.android.synthetic.main.view_bottom_player.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG_DIALOG_PROMPT_LOGIN = "prompt_login_dialog"
class EpisodeDetailsFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_episode_details
    private var isPlaying = false
    val selectedEpisode: EpisodeModel by lazy {
        arguments?.getParcelable(SELECTED_EPISODE) as EpisodeModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar.visibility = View.VISIBLE
        toolBar.title = selectedEpisode.name
        holderActivity.setSupportActionBar(toolBar)
        // add back arrow to toolbar
        if (holderActivity.supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
        toolBar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_ios_24px)
        toolBar.setNavigationOnClickListener(View.OnClickListener {
            // back button pressed
            holderActivity.onBackPressed()
        })

        //change icon of fab to play if the episode is running and selected episode is this
        getLastPlayedEpisode()?.let { episode ->
            notifyPlayFabButton(episode)
        }
        notifyBookMark(selectedEpisode)
        homeVM.lastLiveEpisode.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { notifyPlayFabButton(it) })
        //episodeTitleTextView.text = selectedEpisode.name
        dateTextView.text = formatDate(selectedEpisode.createdAt)
        renderTags(selectedEpisode)
        renderContent(selectedEpisode)
        observeLastPlayingEpisode()
        handleFabPlayButton()
        bookmarkButton.setOnClickListener {
            if(isAuthorized()){
                if (selectedEpisode.isSaved) {
                    bookmarkButton.setIconResource(R.drawable.ic_turned_in_not_24px)
                    selectedEpisode.isSaved = false
                    removeEpisode(selectedEpisode)
                } else {
                    bookmarkButton.setIconResource(R.drawable.ic_bookmarks_24px)
                    selectedEpisode.isSaved = true
                    addEpisode(selectedEpisode)
                }
                saveEpisodes(loadSavedEpisodes())
            }else{
                AlertDialogFragment.show(
                    requireFragmentManager(),
                    message = getString(R.string.prompt_login),
                    positiveButton = getString(R.string.ok),
                    tag = TAG_DIALOG_PROMPT_LOGIN)
            }

        }
        relatedLinksButton.setOnClickListener {
            if (isAuthorized()){
                val bundle = Bundle()
                bundle.putParcelable(SELECTED_EPISODE, selectedEpisode)
                val browser = BrowserFragment()
                browser.arguments = bundle
                (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    browser
                ).addToBackStack("browser_transaction").commit()
            }else{
                AlertDialogFragment.show(
                    requireFragmentManager(),
                    message = getString(R.string.prompt_login),
                    positiveButton = getString(R.string.ok),
                    tag = TAG_DIALOG_PROMPT_LOGIN)
            }
        }
    }

    private fun isAuthorized(): Boolean {
        getUserModel()?.let { userModel ->
            if (!userModel.authToken.isNullOrEmpty())
                return true
        }
        return false
    }

    private fun notifyBookMark(selectedEpisode: EpisodeModel) {
        val episodes = loadSavedEpisodes()
        for (episode in episodes){
             selectedEpisode.isSaved = episode.id == selectedEpisode.id && episode.isSaved
             if(selectedEpisode.isSaved) break;
        }

        if (selectedEpisode.isSaved)
            bookmarkButton.setIconResource(R.drawable.ic_bookmarks_24px)
        else
            bookmarkButton.setIconResource(R.drawable.ic_turned_in_not_24px)
    }


    private fun handleFabPlayButton() {
        playButton.setOnClickListener {
            getLastPlayedEpisode()?.let { episode ->
                isPlaying = episode.isPlaying
            }
            if (isPlaying) {
                selectedEpisode.isPlaying = false
                homeVM.setLastPlayedEpisode(selectedEpisode)
                playButton.setImageResource(R.drawable.vd_play_arrow)
                bottomPlayer.visibility = View.VISIBLE
                holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_play_arrow_24px)
                holderActivity.lastPlayedEpisodeTitle.text = selectedEpisode.name
                homeVM.lastLiveEpisode.value?.let {
                    holderActivity.pause(it)
                    setLastPlayedEpisode(it)
                }


            } else {
                playButton.setImageResource(R.drawable.ic_pause_24px)
                bottomPlayer.visibility = View.VISIBLE
                holderActivity.bottomPlayerButton.setImageResource(R.drawable.ic_pause_32px)
                holderActivity.playingEpisode = selectedEpisode
                selectedEpisode.isPlaying = true
                homeVM.setLastPlayedEpisode(selectedEpisode)
                holderActivity.lastPlayedEpisodeTitle.text = selectedEpisode.name
                homeVM.lastLiveEpisode.value?.let {
                    holderActivity.play(it)
                    setLastPlayedEpisode(it)
                }
            }

        }
    }


    fun notifyPlayFabButton(episode: EpisodeModel) {
        if (episode.isPlaying && episode.id == selectedEpisode.id)
            playButton.setImageResource(R.drawable.ic_pause_24px)
        else
            playButton.setImageResource(R.drawable.vd_play_arrow)
    }


    fun handleNotificationAction(episode: EpisodeModel) {
        if (episode.isPlaying && episode.id == selectedEpisode.id)
            playButton.setImageResource(R.drawable.ic_pause_24px)
        else
            playButton.setImageResource(R.drawable.vd_play_arrow)
    }


    fun formatDate(dateStr: String): String {
        val DATE_FORMAT = "dd MMMM yyyy"
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val format2 = SimpleDateFormat(DATE_FORMAT)
        val date: Date = format1.parse(dateStr)
        return format2.format(date)

    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_episode_details, menu)
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.share -> {
////                val shareIntent = Intent(Intent.ACTION_SEND).apply {
//////                    type = "text/plain"
//////                    putExtra(Intent.EXTRA_TEXT, viewModel.episode?.link)
//////                    putExtra(Intent.EXTRA_HTML_TEXT, viewModel.episode?.excerpt?.rendered)
////                }
//
//                //  startActivity(shareIntent)
//
//                return true
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    private fun renderTags(episode: EpisodeModel) {
        if (episode.tags.isNullOrEmpty()) {
            tagsHorizontalScrollView.visibility = View.GONE
        } else {
            tagsChipGroup.removeAllViews()

            episode.tags.forEach { tag ->
                tagsChipGroup.addView(Chip(context).apply {
                    text = tag

                    setOnClickListener {
                        // val direction = EpisodeDetailFragmentDirections.openEpisodesAction(SearchQuery(tagId = tag.id.toString()), true, true)
                        // findNavController().navigate(direction)
                    }
                })
            }

            tagsHorizontalScrollView.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun renderContent(episode: EpisodeModel) {
        episode.content?.let { html ->
            contentWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    url?.let {
                        if (!openUrl(url)) {
                            acknowledgeViewEpisodeLinkFailed()
                        }
                    }

                    return true
                }
            }

            contentWebView.settings.defaultTextEncodingName = "utf-8"
            contentWebView.loadDataWithBaseURL(
                null,
                EpisodeHtmlUtils.cleanHtml(html),
                "text/html",
                "utf-8",
                null
            )
        }
    }

    private fun acknowledgeViewEpisodeLinkFailed() = Snackbar.make(
        containerFrameLayout,
        R.string.view_episode_link_failed,
        Snackbar.LENGTH_SHORT
    ).show()




}