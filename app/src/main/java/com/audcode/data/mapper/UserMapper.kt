package com.audcode.data.mapper

import com.audcode.data.network.response.AuthRes
import com.audcode.data.network.response.UserResponse
import com.audcode.domain.model.User


object UserMapper {
    fun transform(res: UserResponse): User {
        return User(res.id, res.email)
    }

    fun transform(res: AuthRes): String {
        return res.body.token
    }
}