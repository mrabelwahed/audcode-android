package com.gify.domain.repository

import com.gify.domain.model.Gif
import com.gify.ui.dto.QueryDTO
import io.reactivex.Flowable

interface GifyRepository {
    fun getGifList(queryDTO: QueryDTO): Flowable<List<Gif>>
}