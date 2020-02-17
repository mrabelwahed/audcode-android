package com.gify.ui.viewstate

import com.gify.data.exceptions.Failure

sealed class ServerDataState {
    object Loading : ServerDataState()
    data class Error(val failure: Failure?) : ServerDataState()
    data class Success<T>(val item: T) : ServerDataState()
}