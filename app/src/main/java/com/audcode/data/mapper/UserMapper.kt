package com.audcode.data.mapper

import com.audcode.data.network.response.AuthObject
import com.audcode.data.network.response.AuthRes
import com.audcode.data.network.response.UserResponse
import com.audcode.domain.model.User


object UserMapper {
    fun transform(res: UserResponse): User {
        return User(res.id, res.fullName, res.email)
    }

    fun transform(res: AuthObject): User {
        return User(res.user.id, res.user.fullName, res.user.email,res.token)
    }
}