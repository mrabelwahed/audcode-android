package com.audcode.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.audcode.data.exceptions.Failure
import com.audcode.ui.viewstate.ServerDataState
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.net.UnknownHostException

open class BaseViewModel  : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

     fun setFailure(throwable: Throwable): ServerDataState {
        return when (throwable) {
            is UnknownHostException -> ServerDataState.Error(Failure.NetworkConnection)
            is HttpException -> ServerDataState.Error(Failure.ServerError)
            else -> ServerDataState.Error(Failure.UnExpectedError)
        }
    }
}