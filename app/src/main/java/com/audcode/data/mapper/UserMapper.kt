package com.audcode.data.mapper

import com.audcode.data.network.response.AuthObject
import com.audcode.data.network.response.AuthRes
import com.audcode.data.network.response.UserResponse
import com.audcode.domain.model.User


object UserMapper {
    fun transform(res: UserResponse): User {
        return User(res.id, res.fullName, res.email)
    }

    fun transform(authObject: AuthObject): User {
        return User(authObject.user.id, authObject.user.fullName, authObject.user.email,authObject.token)
    }
}