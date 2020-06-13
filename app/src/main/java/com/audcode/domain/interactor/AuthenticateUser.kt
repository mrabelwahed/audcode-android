package com.audcode.domain.interactor

import com.audcode.domain.model.User
import com.audcode.domain.repository.UserRepository
import com.audcode.ui.dto.AuthDTO
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthenticateUser(private val userRepository: UserRepository) : UseCase<AuthDTO, User> {
    override fun execute(param: AuthDTO): Flowable<User> {
        return userRepository.login(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}