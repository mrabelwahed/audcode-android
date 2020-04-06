package com.audcode.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audcode.data.exceptions.Failure
import com.audcode.domain.interactor.GetEpisodesUC
import com.audcode.ui.dto.QueryDTO
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.mapper.EpisodeModelMapper
import com.audcode.ui.viewmodel.BaseViewModel
import com.audcode.ui.viewstate.ServerDataState
import com.google.android.exoplayer2.SimpleExoPlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject


class HomeVM @Inject constructor(private val getEpisodesUC: GetEpisodesUC) :
    BaseViewModel() {
    val viewState = MutableLiveData<ServerDataState>()
    private val lastPlayedEpisode = MutableLiveData<EpisodeModel>()
    val lastLiveEpisode :LiveData<EpisodeModel>
    get()= lastPlayedEpisode
    val episodeLiveData: LiveData<ServerDataState>
        get() = viewState


    private var lastQuery:String? = null
    private var lastOffset = 0L

    private fun setFailure(throwable: Throwable): ServerDataState {
        return when (throwable) {
            is UnknownHostException -> ServerDataState.Error(Failure.NetworkConnection)
            is HttpException -> ServerDataState.Error(Failure.ServerError)
            else -> ServerDataState.Error(Failure.UnExpectedError)
        }
    }


    fun loadNextPage() {
        val disposable =  getEpisodesUC.execute(QueryDTO(lastQuery,lastOffset))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> viewState.value = ServerDataState.Success(EpisodeModelMapper.transform(res))},
                { error -> viewState.value = setFailure(error) }
            )
        compositeDisposable.add(disposable)
    }

    fun incrementOffset(){
        this.lastOffset += 20
    }


    fun setNewQuery(query: String?){
        this.lastQuery = query
        this.lastOffset =0L
    }

    fun setLastPlayedEpisode(episode: EpisodeModel){
       lastPlayedEpisode.value = episode
    }






}