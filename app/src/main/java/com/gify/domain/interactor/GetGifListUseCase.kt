package com.gify.domain.interactor

import com.gify.domain.model.Gif
import com.gify.domain.repository.GifyRepository
import com.gify.ui.dto.QueryDTO
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetGifListUseCase(private val gifyRepository: GifyRepository) :
    UseCase<QueryDTO, List<Gif>> {
    override fun execute(param: QueryDTO): Flowable<List<Gif>> {
        return gifyRepository.getGifList(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}