package com.audcode.domain.interactor

import com.audcode.domain.repository.UserRepository
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AuthenticateUser(private val userRepository: UserRepository) : UseCase<UserDTO, String> {
    override fun execute(param: UserDTO): Flowable<String> {
        return userRepository.login(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}