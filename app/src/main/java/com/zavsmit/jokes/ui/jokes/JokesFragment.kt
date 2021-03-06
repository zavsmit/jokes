package com.zavsmit.jokes.ui.jokes

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.R
import com.zavsmit.jokes.ui.common_jokes.JokesParentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jokes.*

@AndroidEntryPoint
class JokesFragment : JokesParentFragment() {
    private val jokesViewModel: JokesViewModel by viewModels()

    override fun initView() {
        tv_empty.text = getString(R.string.no_data)
    }

    override fun vmObserve() {
        jokesViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.submitList(it.list)
            showScreen(it.screenNumber)
        })
        jokesViewModel.singleEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SingleEvent.Share -> shareJoke(it.text)
                is SingleEvent.SnackBar -> showSnackBar(it.text)
                is SingleEvent.Progress -> toggleRefreshing(it.isVisible)
            }
        })
    }

    override fun refreshData() {
        jokesViewModel.refreshData()
    }

    override fun onRightButtonClicked(id: Long) {
        jokesViewModel.onLikeClicked(id)
    }

    override fun onShareClicked(text: String) {
        jokesViewModel.onShareClicked(text)
    }

    override fun onDestroyFragment() {
        jokesViewModel.onDestroy()
    }

    override fun getNextData() {
        jokesViewModel.getNextData()
    }

    private fun shareJoke(text: String) {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }, null))
    }
}