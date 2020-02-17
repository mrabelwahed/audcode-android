package com.gify.data.repository

import com.gify.AppConst.LIMIT
import com.gify.data.mapper.GifListMapper
import com.gify.data.network.GifAPI
import com.gify.domain.model.Gif
import com.gify.domain.repository.GifyRepository
import com.gify.ui.dto.QueryDTO
import io.reactivex.Flowable

class GifDataRepository (private val gifAPI: GifAPI) : GifyRepository {

    override fun getGifList(queryDTO: QueryDTO): Flowable<List<Gif>> {
        return gifAPI.search(queryDTO.query,LIMIT,queryDTO.offset)
            .map { res -> GifListMapper.transform(res.data) }
    }

}