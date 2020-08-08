package com.zavsmit.jokes.ui.my_jokes

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyJokesFragment : JokesParentFragment(), AddJokeDialog.AddJokeDialogListener {

    private val myJokesViewModel: MyJokesViewModel by viewModels()
    override fun setEtResultDialog(text: String) {

    }

    override fun vmObserve() {

        myJokesViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.setData(it)
        })
    }

    override fun onLikeClicked(id: Long) {

    }

    override fun onShareClicked(text: String) {
        //
    }
}