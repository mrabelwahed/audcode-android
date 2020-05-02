package com.audcode.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audcode.domain.interactor.AuthenticateUser
import com.audcode.domain.repository.UserRepository
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.login.mapper.UserModelMapper
import com.audcode.ui.viewmodel.BaseViewModel
import com.audcode.ui.viewstate.ServerDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LoginVM @Inject constructor(private val authUseCase: AuthenticateUser)  : BaseViewModel(){
    private val userState = MutableLiveData<ServerDataState>()
    val userViewState: LiveData<ServerDataState>
        get() = userState

    fun authenticateUser(userDTO: UserDTO) {
        val disposable = authUseCase.execute(userDTO)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    userState.value = ServerDataState.Success(res)
                },
                { error -> userState.value = setFailure(error) })
        compositeDisposable.add(disposable)

    }
}