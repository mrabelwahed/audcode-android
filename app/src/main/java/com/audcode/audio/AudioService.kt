package com.audcode.audio

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


import com.audcode.AppConst
import com.audcode.R
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class AudioService : Service() {

    private val binder = AudioServiceBinder()
    private var player: SimpleExoPlayer? = null
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var episodeModel: EpisodeModel
    private lateinit var serviceAction: String
    private var lastPlayingItem: EpisodeModel? = null

    private val _playerStatusLiveData = MutableLiveData<PlayerState>()
    val playerStatusLiveData: LiveData<PlayerState>
        get() = _playerStatusLiveData

    private val mediaDescriptor = object : MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player?): PendingIntent? {
            return PendingIntent.getActivity(
                applicationContext,
                0,
                Intent(applicationContext, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        }

        override fun getCurrentContentText(player: Player?): String? {
            return null
        }

        override fun getCurrentContentTitle(player: Player?): String {
            return episodeModel.name
        }

        override fun getCurrentLargeIcon(
            player: Player?,
            callback: PlayerNotificationManager.BitmapCallback?
        ): Bitmap? {
            return getBitmapFromVectorDrawable(applicationContext, R.drawable.logo)
        }

        override fun getCurrentSubText(player: Player?): String? {
            return "subtext"
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    inner class AudioServiceBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }


    private fun startPlayer() {
        val context: Context = this
        val uri: Uri = Uri.parse(episodeModel.url)

        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_SPEECH)
            .build()

        player?.setAudioAttributes(audioAttributes, true)

        val mediaSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)



        player?.prepare(mediaSource)
        player?.playWhenReady = true

        //notification listener
        val notificationListener = object : PlayerNotificationManager.NotificationListener {

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                //when cancel notification like close it from status bar when episode does not play
                onNotificationRemove()
            }

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification?,
                ongoing: Boolean
            ) {
                if (ongoing) {
                    episodeModel?.let {
                        episodeModel.isPlaying = true
                        _playerStatusLiveData.value = PlayerState.Playing(it)
                    }
                    // Make sure the service will not get destroyed while playing media.
                    startForeground(notificationId, notification)
                } else {
                    episodeModel?.let {
                        episodeModel.isPlaying = false
                        _playerStatusLiveData.value = PlayerState.Paused(it)
                    }
                    // Make notification cancellable.
                    stopForeground(false)
                }
            }
        }
        //player event listener

        val playerEventListener = object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (playWhenReady) {
                            episodeModel?.let {
                                it.isPlaying = true
                                _playerStatusLiveData.value = PlayerState.Playing(it)
                            }
                        } else {// Paused
                            episodeModel?.let {
                                it.isPlaying = false
                                _playerStatusLiveData.value = PlayerState.Paused(it)
                            }
                        }
                        //}
                    }
                    Player.STATE_ENDED -> {
                        episodeModel?.let { _playerStatusLiveData.value = PlayerState.Ended(it) }
                        onNotificationRemove()
                    }
                    else -> {
                        episodeModel?.let { _playerStatusLiveData.value = PlayerState.Other(it) }
                    }
                }

            }

            override fun onPlayerError(e: ExoPlaybackException?) {
                episodeModel?.let { _playerStatusLiveData.value = PlayerState.Error(it, e) }
            }
        }

        player?.addListener(playerEventListener)
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this, CHANNEL_ID,
            R.string.channel_name, R.string.channel_description,
            NOTIFICATION_ID, mediaDescriptor, notificationListener
        )

        playerNotificationManager.setPlayer(player)

        //remove previous action in notification
        playerNotificationManager.setUseNavigationActions(false)


    }

    private fun releasePlayer() {
        if (player != null) {
            playerNotificationManager.setPlayer(null)
            player?.release()
            player = null

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val b = intent?.getBundleExtra(AppConst.Keys.BUNDLE_KEY)

        if (b != null) {
            episodeModel =
                b.getParcelable<Parcelable>(AppConst.Keys.SERVICE_EPISODE) as EpisodeModel
            serviceAction = b.getString(AppConst.Keys.SERVICE_ACTION)
        }
        if (serviceAction == AppConst.Keys.PLAY) {
            if (lastPlayingItem?.id != episodeModel.id)
                releasePlayer()

            if (player == null)
                startPlayer()
            else
                resume()
            lastPlayingItem = episodeModel
        } else if (serviceAction == AppConst.Keys.PAUSE) {
            pause()
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }


    fun pause() {
        player?.playWhenReady = false
    }


    fun resume() {
        player?.playWhenReady = true
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val NOTIFICATION_ID = 1
        private const val PLAYBACK_CHANNEL_ID = "playback_channel"
        private const val PLAYBACK_NOTIFICATION_ID = 1
    }

    private fun getBitmapFromVectorDrawable(
        context: Context,
        @DrawableRes drawableId: Int
    ): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()

            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }

    private fun onNotificationRemove(){
        episodeModel?.let {
            episodeModel.isPlaying = false
            _playerStatusLiveData.value = PlayerState.Ended(it)
        }
        stopForeground(true)
        releasePlayer()
        stopSelf()
    }


}