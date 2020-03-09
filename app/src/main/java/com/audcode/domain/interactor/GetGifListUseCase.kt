package com.audcode.domain.interactor

import com.audcode.domain.model.Gif
import com.audcode.domain.repository.GifyRepository
import com.audcode.ui.dto.QueryDTO
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