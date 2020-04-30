package com.audcode.domain.interactor

import com.audcode.domain.model.User
import com.audcode.domain.repository.UserRepository
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateUser(private val userRepository: UserRepository)  : UseCase<UserDTO,User>{
    override fun execute(param: UserDTO): Flowable<User> {
        return  userRepository.createUser(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}