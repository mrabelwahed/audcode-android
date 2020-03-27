package com.audcode.ui.mapper

import com.audcode.domain.model.Episode
import com.audcode.ui.home.model.EpisodeModel

object EpisodeModelMapper {
    private fun transform(episode: Episode): EpisodeModel {

        return EpisodeModel(
            episode.id,
            episode.name,
            episode.createdAt,
            episode.content,
            episode.author,
            episode.tags
        )
    }

    fun transform(episodeCollection: Collection<Episode>): List<EpisodeModel> {
        val episodes = mutableListOf<EpisodeModel>()
        for (episode in episodeCollection) {
            val episodeModel = transform(episode)
            if (episodeModel != null) {
                episodes.add(episodeModel)
            }
        }

        return episodes
    }
}