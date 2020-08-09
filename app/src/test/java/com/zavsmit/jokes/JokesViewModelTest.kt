package com.zavsmit.jokes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zavsmit.jokes.core.sensor.ShakeEventProvider
import com.zavsmit.jokes.data.ResourceManager
import com.zavsmit.jokes.data.SharedPrefsHelper
import com.zavsmit.jokes.domain.usecases.*
import com.zavsmit.jokes.ui.jokes.JokesViewModel
import com.zavsmit.jokes.ui.jokes.SingleEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class JokesViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val schedulerRule = Rx3SchedulerRule()

    lateinit var viewModel: JokesViewModel

    private val resourceManager = mockk<ResourceManager>(relaxed = true)
    private val sharedPrefsHelper = mockk<SharedPrefsHelper>(relaxed = true)
    private val getJokesUseCase = mockk<GetJokesUseCase>(relaxed = true)
    private val addMyJokeByIdUseCase = mockk<AddMyJokeByIdUseCase>(relaxed = true)
    private val deleteJokeUseCase = mockk<DeleteJokeUseCase>(relaxed = true)
    private val getNextJokesUseCase = mockk<GetNextJokesUseCase>(relaxed = true)
    private val refreshJokeUseCase = mockk<RefreshJokeUseCase>(relaxed = true)
    private val getRandomJokeUseCase = mockk<GetRandomJokeUseCase>(relaxed = true)
    private val getMyJokeIdsUseCase = mockk<GetMyJokeIdsUseCase>(relaxed = true)
    private val shakeEventProvider = mockk<ShakeEventProvider>(relaxed = true)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = JokesViewModel(
                resourceManager,
                sharedPrefsHelper,
                getJokesUseCase,
                addMyJokeByIdUseCase,
                deleteJokeUseCase,
                getNextJokesUseCase,
                refreshJokeUseCase,
                getRandomJokeUseCase,
                getMyJokeIdsUseCase,
                shakeEventProvider
        )
    }

    @Test
    fun `should show shared action`() {
        viewModel.onShareClicked("text")
        assert(viewModel.singleEvent.value is SingleEvent.Share)
    }

    @Test
    fun `check shakeEventProvider was destroyed`() {
        viewModel.onDestroy()
        verify { shakeEventProvider.removeObserver(any()) }
        verify { shakeEventProvider.unsubscribe() }
    }

    @Test
    fun `addMyJokeByIdUseCase should be called on like with right id`() {
        viewModel.onLikeClicked(1)
        val slot = slot<AddMyJokeByIdUseCase.Arg>()
        verify { addMyJokeByIdUseCase.execute(capture(slot)) }
        assert(slot.captured.id == 1L)
    }

    @Test
    fun `addMyJokeByIdUseCase should be called on like clicked`() {
        viewModel.onLikeClicked(1)
        verify { deleteJokeUseCase.execute(any()) }
    }

    @Test
    fun `snack bar should be shown after joke added`() {
        every { addMyJokeByIdUseCase.execute(any()) } returns Completable.complete()
        viewModel.onLikeClicked(1)
        assert(viewModel.singleEvent.value == SingleEvent.SnackBar(resourceManager.getString(R.string.joke_added)))
    }
}