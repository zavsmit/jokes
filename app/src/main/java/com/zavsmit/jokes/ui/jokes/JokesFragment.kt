package com.zavsmit.jokes.ui.jokes

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JokesFragment : JokesParentFragment() {
    private val jokesViewModel: JokesViewModel by viewModels()

    override fun vmObserve() {
        jokesViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.setData(it)
        })

        jokesViewModel.getData()
    }

    override fun onLikeClicked(id: Long) {
        jokesViewModel.onLikeClicked(id)
    }

    override fun onShareClicked(text: String) {
        jokesViewModel.onShareClicked(text)
    }
}