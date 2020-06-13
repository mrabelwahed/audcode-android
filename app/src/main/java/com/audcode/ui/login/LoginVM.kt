package com.audcode.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audcode.domain.interactor.AuthenticateUser
import com.audcode.ui.dto.AuthDTO
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.login.mapper.UserModelMapper
import com.audcode.ui.viewmodel.BaseViewModel
import com.audcode.ui.viewstate.ServerDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LoginVM @Inject constructor(private val authUseCase: AuthenticateUser) : BaseViewModel() {
    private val userState = MutableLiveData<ServerDataState>()
    val userViewState: LiveData<ServerDataState>
        get() = userState

    fun authenticateUser(authDTO: AuthDTO) {
        val disposable = authUseCase.execute(authDTO)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    userState.value = ServerDataState.Success(UserModelMapper.transform(res))
                },
                { error -> userState.value = setFailure(error) })
        compositeDisposable.add(disposable)

    }
}