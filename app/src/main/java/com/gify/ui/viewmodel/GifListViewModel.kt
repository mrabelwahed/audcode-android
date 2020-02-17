package com.gify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gify.data.exceptions.Failure
import com.gify.domain.interactor.GetGifListUseCase
import com.gify.ui.dto.QueryDTO
import com.gify.ui.mapper.GifModelMapper
import com.gify.ui.viewstate.ServerDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject


class GifListViewModel @Inject constructor(private val getGifListUseCase: GetGifListUseCase) :
    BaseViewModel() {
    val viewState = MutableLiveData<ServerDataState>()
    val liveGifData: LiveData<ServerDataState>
        get() = viewState

    private lateinit var lastQuery:String
    private var lastOffset = 0L

    private fun setFailure(throwable: Throwable): ServerDataState {
        return when (throwable) {
            is UnknownHostException -> ServerDataState.Error(Failure.NetworkConnection)
            is HttpException -> ServerDataState.Error(Failure.ServerError)
            else -> ServerDataState.Error(Failure.UnExpectedError)
        }
    }


    fun loadNextPage() {
        val disposable =   getGifListUseCase.execute(QueryDTO(lastQuery,lastOffset))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> viewState.value = ServerDataState.Success(GifModelMapper.transform(res)) },
                { error -> viewState.value = setFailure(error) }
            )
        compositeDisposable.add(disposable)
    }

    fun incrementOffset(){
        this.lastOffset += 20
    }



    fun setNewQuery(query: String){
        this.lastQuery = query
        this.lastOffset =0L
    }


}