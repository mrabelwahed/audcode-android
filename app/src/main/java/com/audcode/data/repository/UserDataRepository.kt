package com.audcode.data.repository

import com.audcode.data.mapper.UserMapper
import com.audcode.data.network.AudcodeAPI
import com.audcode.domain.model.User
import com.audcode.domain.repository.UserRepository
import com.audcode.ui.dto.AuthDTO
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable

class UserDataRepository(private val audcodeAPI: AudcodeAPI) : UserRepository {
    override fun createUser(userDTO: UserDTO): Flowable<User> {
        return audcodeAPI.createUser(userDTO)
            .map { res -> UserMapper.transform(res.body) }
    }

    override fun login(authDTO: AuthDTO): Flowable<User> {
        return audcodeAPI.authenticateUser(authDTO)
            .map { res -> UserMapper.transform(res.body) }

    }
}
