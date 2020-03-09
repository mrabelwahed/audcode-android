package com.audcode.data.repository

import com.audcode.AppConst.LIMIT
import com.audcode.data.mapper.GifListMapper
import com.audcode.data.network.GifAPI
import com.audcode.domain.model.Gif
import com.audcode.domain.repository.GifyRepository
import com.audcode.ui.dto.QueryDTO
import io.reactivex.Flowable

class GifDataRepository (private val gifAPI: GifAPI) : GifyRepository {

    override fun getGifList(queryDTO: QueryDTO): Flowable<List<Gif>> {
        return gifAPI.search(queryDTO.query,LIMIT,queryDTO.offset)
            .map { res -> GifListMapper.transform(res.data) }
    }

}