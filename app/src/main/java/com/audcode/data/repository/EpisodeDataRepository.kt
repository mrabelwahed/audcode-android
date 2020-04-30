package com.audcode.data.repository

import com.audcode.data.mapper.EpisodesMapper
import com.audcode.data.network.AudcodeAPI
import com.audcode.domain.model.Episode
import com.audcode.domain.repository.EpisodeRepository
import com.audcode.ui.dto.QueryDTO
import io.reactivex.Flowable

class EpisodeDataRepository(private val episodeApi: AudcodeAPI) : EpisodeRepository {

    override fun getEpisodes(queryDTO: QueryDTO): Flowable<List<Episode>> {
        return episodeApi.getEpisodes(queryDTO?.query, queryDTO.skip, queryDTO.limit)
            .map { res -> EpisodesMapper.transform(res.body) }
    }

}