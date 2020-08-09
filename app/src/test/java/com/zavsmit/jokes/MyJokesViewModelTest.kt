package com.zavsmit.jokes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zavsmit.jokes.data.ResourceManager
import com.zavsmit.jokes.domain.usecases.AddJokeUseCase
import com.zavsmit.jokes.domain.usecases.AddMyJokeByIdUseCase
import com.zavsmit.jokes.domain.usecases.DeleteJokeUseCase
import com.zavsmit.jokes.domain.usecases.GetMyJokesUseCase
import com.zavsmit.jokes.ui.jokes.ViewEffect
import com.zavsmit.jokes.ui.my_jokes.MyJokesViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class MyJokesViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val schedulerRule = Rx3SchedulerRule()

    lateinit var viewModel: MyJokesViewModel

    private val resourceManager = mockk<ResourceManager>(relaxed = true)
    private val addJokeUseCase = mockk<AddJokeUseCase>(relaxed = true)
    private val deleteJokeUseCase = mockk<DeleteJokeUseCase>(relaxed = true)
    private val getMyJokesUseCase = mockk<GetMyJokesUseCase>(relaxed = true)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MyJokesViewModel(
                resourceManager,
                addJokeUseCase,
                deleteJokeUseCase,
                getMyJokesUseCase
        )
    }

    @Test
    fun `addJokeUseCase should be called after click added with right text`() {
        viewModel.setNewJoke("text")
        val slot = slot<AddJokeUseCase.Arg>()
        verify { addJokeUseCase.execute(capture(slot)) }
        assert(slot.captured.text == "text")
    }

    @Test
    fun `setNewJoke() should invoke getMyJokesUseCase`() {
        every { addJokeUseCase.execute(any()) } returns Completable.complete()
        viewModel.setNewJoke("text")
        verify { getMyJokesUseCase.execute() }
    }

    @Test
    fun `addJokeUseCase should be called on like clicked`() {
        viewModel.setNewJoke("text")
        verify { addJokeUseCase.execute(any()) }
    }


    @Test
    fun `deleteJokeUseCase should be called after click delete with right id`() {
        viewModel.deleteJoke(1)
        val slot = slot<DeleteJokeUseCase.Arg>()
        verify { deleteJokeUseCase.execute(capture(slot)) }
        assert(slot.captured.id == 1L)
    }

    @Test
    fun `deleteJoke() should invoke getMyJokesUseCase`() {
        every { deleteJokeUseCase.execute(any()) } returns Completable.complete()
        viewModel.deleteJoke(1)
        verify { getMyJokesUseCase.execute() }
    }

    @Test
    fun `deleteJokeUseCase should be called on like clicked`() {
        viewModel.deleteJoke(1)
        verify { deleteJokeUseCase.execute(any()) }
    }

    @Test
    fun `snack bar should be shown after joke deleted`() {
        every { deleteJokeUseCase.execute(any()) } returns Completable.complete()
        viewModel.deleteJoke(1)
        assert(viewModel.viewEffect.value == ViewEffect.SnackBar(resourceManager.getString(R.string.joke_deleted)))
    }

    @Test
    fun `getMyJokesUseCase should be called`() {
        viewModel.getData()
        verify { getMyJokesUseCase.execute() }
    }
}