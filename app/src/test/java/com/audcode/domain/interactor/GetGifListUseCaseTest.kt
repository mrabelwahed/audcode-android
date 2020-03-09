package com.audcode.domain.interactor

import com.audcode.domain.repository.GifyRepository
import com.audcode.ui.dto.QueryDTO
import com.rules.RxSchedulerRule
import io.reactivex.Flowable
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnit

class GetGifListUseCaseTest {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    lateinit var getGifListUseCase: GetGifListUseCase

    @Mock
    lateinit var gifyRepository: GifyRepository


    @Rule
    @JvmField
    var testSchedulerRule: RxSchedulerRule = RxSchedulerRule()

    val query = QueryDTO("android", 0)

    @Before
    fun setup() {
        getGifListUseCase = GetGifListUseCase(gifyRepository)
        given(gifyRepository.getGifList(query)).willReturn(Flowable.just(emptyList()))
    }

    @Test
    fun `usecase is ready for test`() {
        assertNotNull(getGifListUseCase)
    }

    @Test
    fun `should get data from repository`() {
        getGifListUseCase.execute(query)
        verify(gifyRepository).getGifList(query)
        verifyNoMoreInteractions(gifyRepository)
    }

}