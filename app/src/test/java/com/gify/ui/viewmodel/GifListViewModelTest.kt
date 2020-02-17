package com.gify.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bumptech.glide.load.HttpException
import com.gify.data.exceptions.Failure
import com.gify.domain.interactor.GetGifListUseCase
import com.gify.domain.model.Gif
import com.gify.domain.repository.GifyRepository
import com.gify.ui.dto.QueryDTO
import com.gify.ui.viewstate.ServerDataState
import com.rules.RxSchedulerRule
import com.util.mock
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import java.net.UnknownHostException


class GifListViewModelTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var gifListViewModel: GifListViewModel

    lateinit var gifListUseCase: GetGifListUseCase

    @Mock
    var observer: Observer<ServerDataState> = mock()

    @Rule
    @JvmField
    var testSchedulerRule: RxSchedulerRule = RxSchedulerRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var gifyRepository: GifyRepository

    @Before
    fun setup() {
        gifListUseCase = GetGifListUseCase(gifyRepository)
        gifListViewModel = GifListViewModel(gifListUseCase)
        gifListViewModel.liveGifData.observeForever(observer)
    }

    @Test
    fun `view model is ready for test`() {
        assertNotNull(gifListViewModel)
    }

    @Test
    fun `should first page of gifs are loaded successfully`() {
        //Given
        val dummyQuery = QueryDTO("android", 0)
        `when`(gifyRepository.getGifList(dummyQuery)).thenReturn(Flowable.just(createGifList()))
        //when
        gifListViewModel.setNewQuery(dummyQuery.query)
        gifyRepository.getGifList(dummyQuery)
        //then
        verify(gifyRepository).getGifList(dummyQuery)
        verifyNoMoreInteractions(gifyRepository)
    }


    @Test
    fun `search gif should return lis of gifs`() {
        //given
        val dummyQuery = QueryDTO("android", 0)
        `when`(gifyRepository.getGifList(dummyQuery)).thenReturn(Flowable.just(createGifList()))
        //when
        gifListViewModel.setNewQuery(dummyQuery.query)
        gifyRepository.getGifList(dummyQuery)
        gifListViewModel.viewState.value = ServerDataState.Success(Flowable.just(createGifList()))
        //then
        val captor = ArgumentCaptor.forClass(ServerDataState::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(gifListViewModel.viewState.value, value)
        }

    }


    @Test
    fun `no internet error case`() {
        //given
        val dummyQuery = QueryDTO("android", 0)
        `when`(gifyRepository.getGifList(dummyQuery))
            .thenReturn(Flowable.error(UnknownHostException()))
        //when
        gifListViewModel.setNewQuery(dummyQuery.query)
        gifyRepository.getGifList(dummyQuery)
        gifListViewModel.viewState.value = ServerDataState.Error(Failure.NetworkConnection)
        //then
        val captor = ArgumentCaptor.forClass(ServerDataState::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(value,ServerDataState.Error(Failure.NetworkConnection))
        }

    }


    @Test
    fun `server error case`() {
        //given
        val statusCode = 500
        val internalServerError = HttpException(statusCode)
        val dummyQuery = QueryDTO("android", 0)
        `when`(gifyRepository.getGifList(dummyQuery))
            .thenReturn(Flowable.error(internalServerError))
        //when
        gifListViewModel.setNewQuery(dummyQuery.query)
        gifyRepository.getGifList(dummyQuery)
        gifListViewModel.viewState.value = ServerDataState.Error(Failure.NetworkConnection)
        //then
        val captor = ArgumentCaptor.forClass(ServerDataState::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(value,ServerDataState.Error(Failure.NetworkConnection))
        }
    }

    private fun createGifList(): List<Gif> {
        val giflist = mutableListOf<Gif>()
        for (x in 0..10) {
            giflist.add(createDummyGif(x))
        }
        return giflist
    }

    private fun createDummyGif(x: Int): Gif {
        return Gif("$x", "t${x}", "prev_url${x}", "orig_url${x}")
    }


}