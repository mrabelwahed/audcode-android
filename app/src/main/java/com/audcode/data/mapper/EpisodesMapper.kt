package com.audcode.data.mapper

import android.util.Log
import com.audcode.data.network.response.EpisodeItem
import com.audcode.domain.model.Episode

object EpisodesMapper {
    private fun transform(res: EpisodeItem): Episode {
        var editableUrl: String =  res.url
        if(!res.url.contains("https"))
            editableUrl = "https://"+res.url

        return Episode(
            res.id,
            res.name,
            res.createdAt,
            res.content,
            res.contentUrl,
            res.author,
            res.tags,
            editableUrl
        )
    }

    fun transform(responseCollection: Collection<EpisodeItem>): List<Episode> {
        val episodes = mutableListOf<Episode>()
        for (res in responseCollection) {
            val episode = transform(res)
            if (episode != null) {
                episodes.add(episode)
            }
        }
        return episodes
    }
}