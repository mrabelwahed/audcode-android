package com.audcode.ui

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.audcode.R
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.view_bottom_player.*


class BrowserFragment : BaseFragment() {
    override fun getLayoutById() = R.layout.fragment_browse

    val selectedEpisode: EpisodeModel by lazy {
        arguments?.getParcelable(MainActivity.SELECTED_EPISODE) as EpisodeModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internalBrowser.getSettings().javaScriptEnabled = true // enable javascript

        internalBrowser.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                    view,
                    rerr.errorCode,
                    rerr.description.toString(),
                    req.url.toString()
                )
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility  = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        internalBrowser.loadUrl(selectedEpisode.contentUrl)
    }

    override fun onResume() {
        super.onResume()
        bottomPlayer.visibility = View.GONE
    }
}