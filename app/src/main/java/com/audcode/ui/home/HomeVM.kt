package com.audcode.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audcode.data.exceptions.Failure
import com.audcode.domain.interactor.GetEpisodes
import com.audcode.ui.dto.QueryDTO
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.home.mapper.EpisodeModelMapper
import com.audcode.ui.viewmodel.BaseViewModel
import com.audcode.ui.viewstate.ServerDataState
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject


class HomeVM @Inject constructor(private val getEpisodesUC: GetEpisodes) :
    BaseViewModel() {
    val viewState = MutableLiveData<ServerDataState>()
    private val lastPlayedEpisode = MutableLiveData<EpisodeModel>()
    val lastLiveEpisode :LiveData<EpisodeModel>
    get()= lastPlayedEpisode
    val episodeLiveData: LiveData<ServerDataState>
        get() = viewState




     var lastQuery:String? = null
    private var lastOffset = 0L




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