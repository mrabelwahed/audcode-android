package com.audcode.domain.repository

import com.audcode.domain.model.Episode
import com.audcode.ui.dto.QueryDTO
import io.reactivex.Flowable

interface EpisodeRepository {
    fun getEpisodes(queryDTO: QueryDTO): Flowable<List<Episode>>
}