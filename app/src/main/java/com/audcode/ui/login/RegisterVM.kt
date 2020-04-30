package com.audcode.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audcode.domain.interactor.CreateUser
import com.audcode.ui.dto.UserDTO
import com.audcode.ui.login.mapper.UserModelMapper
import com.audcode.ui.viewmodel.BaseViewModel
import com.audcode.ui.viewstate.ServerDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RegisterVM @Inject constructor(private val createUserUseCase: CreateUser) : BaseViewModel() {
    private val userState = MutableLiveData<ServerDataState>()
    val userViewState: LiveData<ServerDataState>
        get() = userState

    fun createUser(userDTO: UserDTO) {
        val disposable = createUserUseCase.execute(userDTO)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> userState.value = ServerDataState.Success(UserModelMapper.transform(res)) },
                { error -> userState.value = setFailure(error) })
        compositeDisposable.add(disposable)

    }
}


