package com.audcode.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audcode.data.exceptions.Failure
import com.audcode.data.exceptions.Failure.NetworkConnection
import com.audcode.data.exceptions.Failure.ServerError
import com.audcode.data.network.response.ErrorResponse
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.viewstate.ServerDataState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()
   private  val savedEpisodeList = MutableLiveData<EpisodeModel>()
    val liveSavedEpisodes :LiveData<EpisodeModel>
    get() = savedEpisodeList


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }



    fun setFailure(throwable: Throwable): ServerDataState? {
        if(throwable is UnknownHostException)
            return   ServerDataState.Error(NetworkConnection)
        if(throwable is SocketTimeoutException)
            return ServerDataState.Error(Failure.UnExpectedError("Connection is timeout , retry again by clicking on home icon"))

        var errorResponse: ErrorResponse?= null
        val error = throwable as HttpException
        val errorBody = error.response()?.errorBody()?.string()
        val type = object : TypeToken<ErrorResponse>() {}.type
        if (errorBody != null) {
            errorResponse = if(errorBody.contains("<html>")) {
                ErrorResponse(502,"Server Error")
            } else
                Gson().fromJson(errorBody, type)
        }

        if (errorResponse != null) {
            return when (throwable) {
                is HttpException -> ServerDataState.Error(ServerError(errorResponse.message))
                else -> ServerDataState.Error(Failure.UnExpectedError(errorResponse.message))
            }
        }
        return null
    }
}