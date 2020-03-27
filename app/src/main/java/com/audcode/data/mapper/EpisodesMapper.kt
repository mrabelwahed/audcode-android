package com.audcode.data.mapper

import com.audcode.data.network.response.EpisodeItem
import com.audcode.domain.model.Episode

object EpisodesMapper {
    private fun transform(res: EpisodeItem): Episode {
        return Episode(
            res.id,
            res.name,
            res.createdAt,
            res.content,
            res.author,
            res.tags
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