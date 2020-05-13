package com.audcode.domain.repository

import com.audcode.domain.model.User
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable

interface UserRepository {
    fun createUser(userDTO: UserDTO): Flowable<User>
    fun login(userDTO: UserDTO): Flowable<String>
}