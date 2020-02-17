package com.gify.data.mapper

import com.gify.data.network.response.GifRes
import com.gify.domain.model.Gif

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