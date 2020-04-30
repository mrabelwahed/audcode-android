package com.audcode.data.mapper

import com.audcode.data.network.response.UserRes
import com.audcode.data.network.response.UserResponse
import com.audcode.domain.model.Episode
import com.audcode.domain.model.User
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.login.model.UserModel


object UserMapper {
     fun transform(res: UserResponse): User {
        return User(res.id,res.email)
    }
}