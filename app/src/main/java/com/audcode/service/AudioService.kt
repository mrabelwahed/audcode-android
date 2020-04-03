package com.audcode.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
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
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.audcode.AppConst
import com.audcode.R
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class AudioService : Service() {

    private val binder = AudioServiceBinder()
    private var player: SimpleExoPlayer? = null
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var episodeModel: EpisodeModel
    private lateinit var serviceAction: String
    private var lastPlayingItem: EpisodeModel? = null

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("test")
            .setSmallIcon(R.drawable.rxjava)
            .setContentIntent(pendingIntent)
            .build();
        startForeground(1, notification)
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


    }

    private fun releasePlayer() {
        if (player != null) {
            //playerNotificationManager.setPlayer(null)
            player?.release()
            player = null

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val b = intent?.getBundleExtra(AppConst.keys.BUNDLE_KEY)

        if (b != null) {
            episodeModel =
                b.getParcelable<Parcelable>(AppConst.keys.SERVICE_EPISODE) as EpisodeModel
            serviceAction = b.getString(AppConst.keys.SERVICE_ACTION)
        }
        if (serviceAction == AppConst.keys.PLAY) {
            if (lastPlayingItem?.id != episodeModel.id)
                releasePlayer()

            if (player == null)
                startPlayer()
            else
                resume()
        lastPlayingItem = episodeModel
        } else if (serviceAction == AppConst.keys.PAUSE) {
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
        private const val PLAYBACK_CHANNEL_ID = "playback_channel"
        private const val PLAYBACK_NOTIFICATION_ID = 1
    }

    //    private inner class PlayerEventListener : Player.EventListener {
//
//        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//            if (playbackState == Player.STATE_READY) {
//                if (exoPlayer.playWhenReady) {
//                    episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Playing(it) }
//                } else {// Paused
//                    episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Paused(it) }
//                }
//            } else if (playbackState == Player.STATE_ENDED) {
//                episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Ended(it) }
//            } else {
//                episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Other(it) }
//            }
//
//            // Only monitor playback to record progress when playing.
//            if (playbackState == Player.STATE_READY && exoPlayer.playWhenReady) {
//                monitorPlaybackProgress()
//            } else {
//                cancelPlaybackMonitor()
//            }
//        }
//
//        override fun onPlayerError(e: ExoPlaybackException?) {
//            episodeId?.let { _playerStatusLiveData.value = PlayerStatus.Error(it, e) }
//        }
//
//    }
    private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
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

}