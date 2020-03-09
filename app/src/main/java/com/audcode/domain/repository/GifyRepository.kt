package com.audcode.domain.repository

import com.audcode.domain.model.Gif
import com.audcode.ui.dto.QueryDTO
import io.reactivex.Flowable

interface GifyRepository {
    fun getGifList(queryDTO: QueryDTO): Flowable<List<Gif>>
}