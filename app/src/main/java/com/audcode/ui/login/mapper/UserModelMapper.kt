package com.audcode.ui.login.mapper

import com.audcode.domain.model.Episode
import com.audcode.domain.model.User
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.login.model.UserModel


object UserModelMapper {
     fun transform(user: User): UserModel {
        return UserModel(user.id,user.email)
    }
}