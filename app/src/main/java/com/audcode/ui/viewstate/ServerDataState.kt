package com.audcode.ui.viewstate

import com.audcode.data.exceptions.Failure

sealed class ServerDataState {
    object Loading : ServerDataState()
    data class Error(val failure: Failure?) : ServerDataState()
    data class Success<T>(val item: T) : ServerDataState()
}