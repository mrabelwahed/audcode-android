package com.audcode.data.mapper

import com.audcode.data.network.response.GifRes
import com.audcode.domain.model.Gif

object GifListMapper {
    private fun transform(response : GifRes): Gif {
        return Gif(
            response.id,
            response.title,
            response.images.previewGif.url,
            response.images.originalGif.url
        )
    }

    fun transform(responseCollection: Collection<GifRes>): List<Gif> {
        val gifList = mutableListOf<Gif>()
        for (gifRes in responseCollection) {
            val feed = transform(gifRes)
            if (feed != null) {
                gifList.add(feed)
            }
        }

        return gifList
    }
}