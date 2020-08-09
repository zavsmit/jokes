package com.zavsmit.jokes.ui.my_jokes

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.R
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment
import com.zavsmit.jokes.ui.jokes.ViewEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jokes.*

@AndroidEntryPoint
class MyJokesFragment : JokesParentFragment(), AddJokeDialog.AddJokeDialogListener {
    private val myJokesViewModel: MyJokesViewModel by viewModels()

    override fun initView() {
        tv_empty.text = getString(R.string.add_joke)
        isMyJoke = true
        fab_add.visibility = View.VISIBLE
        fab_add.setOnClickListener { AddJokeDialog.newInstance().show(childFragmentManager, AddJokeDialog.TAG) }
    }

    override fun vmObserve() {
        myJokesViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.submitList(it.list)
            showScreen(it.screenNumber)
        })
        myJokesViewModel.viewEffect.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ViewEffect.SnackBar -> showSnackBar(it.text)
                is ViewEffect.Progress -> toggleRefreshing(it.isVisible)
            }
        })
    }

    override fun refreshData() {
        myJokesViewModel.getData()
    }

    override fun onRightButtonClicked(id: Long) {
        myJokesViewModel.deleteJoke(id)
    }

    override fun setEtResultDialog(text: String) {
        myJokesViewModel.setNewJoke(text)
    }

    override fun onDestroyFragment() {
        myJokesViewModel.onDestroy()
    }

    override fun getNextData() {
        //
    }

    override fun onShareClicked(text: String) {
        //
    }
}