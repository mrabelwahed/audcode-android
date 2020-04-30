package com.audcode.domain.interactor

import com.audcode.domain.model.Episode
import com.audcode.domain.repository.EpisodeRepository
import com.audcode.ui.dto.QueryDTO
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEpisodes(private val episodeRepository: EpisodeRepository) :
    UseCase<QueryDTO, List<Episode>> {
    override fun execute(param: QueryDTO): Flowable<List<Episode>> {
        return episodeRepository.getEpisodes(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}