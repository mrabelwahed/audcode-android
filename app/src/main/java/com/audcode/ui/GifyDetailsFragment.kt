package com.audcode.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.audcode.AppConst.keys.GIF_IMAGE
import com.audcode.R
import com.audcode.ui.model.GifModel
import kotlinx.android.synthetic.main.fragment_gif_details.*

class GifyDetailsFragment : BaseFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gif = arguments?.getParcelable<GifModel>(GIF_IMAGE)
        gif?.let { setData(it) }
    }


    fun setData(gif: GifModel) = Glide.with(gifImage)
        .asGif()
        .load(gif.originalGifUrl)
        .placeholder(R.drawable.undraw_loading_frh4)
        .error(R.drawable.undraw_page_not_found_su7k)
        .into(gifImage)


    override fun getLayoutById() = R.layout.fragment_gif_details

}