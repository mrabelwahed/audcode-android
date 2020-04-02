package com.audcode.ui.episode_details

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.audcode.R
import com.audcode.ui.*
import com.audcode.ui.home.HomeVM
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.audcode.ui.splash.MainActivity.Companion.SELECTED_EPISODE
import com.audcode.ui.viewmodel.ViewModelFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_episode_details.*
import kotlinx.android.synthetic.main.view_bottom_player.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EpisodeDetailsFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_episode_details
    private lateinit var homeVM: HomeVM
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private var isEpisodePlaying = false

    val selectedEpisode :EpisodeModel by lazy {
        arguments?.getParcelable(SELECTED_EPISODE) as EpisodeModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newHomeComponent().inject(this)
        homeVM = ViewModelProvider(this, viewModelFactory)[HomeVM::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // selectedEpisode =   arguments?.getParcelable(SELECTED_EPISODE) as EpisodeModel
        episodeTitleTextView.text = selectedEpisode.name
        dateTextView.text = formatDate(selectedEpisode.createdAt)
        initPlayer()
        renderTags(selectedEpisode)
        renderContent(selectedEpisode)
        observeLastPlayingEpisode()


        with(simpleExoPlayer){
            prepare(mediaSource)
            playButton.setOnClickListener {
                if (isEpisodePlaying){
                    playWhenReady = false
                    isEpisodePlaying =false
                    playButton.setImageResource(R.drawable.vd_play_arrow)
                    bottomPlayer.visibility = View.VISIBLE
                    bottomPlayer.findViewById<ImageButton>(R.id.bottomPlayerButton).setImageResource(R.drawable.ic_play_arrow_24px)
                }else{
                    playWhenReady = true
                    isEpisodePlaying =true
                    playButton.setImageResource(R.drawable.ic_pause_24px)
                    bottomPlayer.visibility = View.VISIBLE
                    bottomPlayer.findViewById<ImageButton>(R.id.bottomPlayerButton).setImageResource(R.drawable.ic_pause_32px)
                    homeVM.setLastPlayedEpisode(selectedEpisode)
                }
            }

            bottomPlayer.findViewById<ImageButton>(R.id.bottomPlayerButton).setOnClickListener {
                if (isEpisodePlaying){
                    playWhenReady = false
                    isEpisodePlaying =false
                    playButton.setImageResource(R.drawable.vd_play_arrow)
                    bottomPlayer.visibility = View.VISIBLE
                    bottomPlayer.findViewById<ImageButton>(R.id.bottomPlayerButton).setImageResource(R.drawable.ic_play_arrow_24px)
                }else{
                    playWhenReady = true
                    isEpisodePlaying =true
                    playButton.setImageResource(R.drawable.ic_pause_24px)
                    bottomPlayer.visibility = View.VISIBLE
                    bottomPlayer.findViewById<ImageButton>(R.id.bottomPlayerButton).setImageResource(R.drawable.ic_pause_32px)
                }
            }



        }
    }






    private fun observeLastPlayingEpisode() {
      homeVM.lastPlayedEpisode.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
          (activity as MainActivity).playingEpisode = it
          (activity as MainActivity). showLastPlayedEpisode()
      })
    }

    private fun initPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(activity)

        dataSourceFactory = DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "audcode"))

        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("https://audcode-space.fra1.digitaloceanspaces.com/eee.m4a"))
    }

    fun formatDate(dateStr:String):String{
        val DATE_FORMAT = "dd MMMM yyyy"
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val format2 = SimpleDateFormat(DATE_FORMAT)
        val date: Date = format1.parse(dateStr)
        return  format2.format(date)

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_episode_details, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
//                val shareIntent = Intent(Intent.ACTION_SEND).apply {
////                    type = "text/plain"
////                    putExtra(Intent.EXTRA_TEXT, viewModel.episode?.link)
////                    putExtra(Intent.EXTRA_HTML_TEXT, viewModel.episode?.excerpt?.rendered)
//                }

                //  startActivity(shareIntent)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

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
    private fun renderContent(episode: EpisodeModel) {
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


//    override fun onDestroy() {
//        simpleExoPlayer.playWhenReady = false
//        isEpisodePlaying = false
//        super.onDestroy()
//    }


}