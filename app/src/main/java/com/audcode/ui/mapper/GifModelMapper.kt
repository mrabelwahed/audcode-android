package com.audcode.ui.mapper

import com.audcode.domain.model.Gif
import com.audcode.ui.model.GifModel

object GifModelMapper {
    private fun transform(gif: Gif): GifModel {
        return GifModel(gif.id, gif.title, gif.previewGifUrl,gif.originalGifUrl)
    }

    fun transform(gifCollection: Collection<Gif>): List<GifModel> {
        val gifList = mutableListOf<GifModel>()
        for (gif in gifCollection) {
            val gifmodel = transform(gif)
            if (gifmodel != null) {
                gifList.add(gifmodel)
            }
        }

        return gifList
    }
}