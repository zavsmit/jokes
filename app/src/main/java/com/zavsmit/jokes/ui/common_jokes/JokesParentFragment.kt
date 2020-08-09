package com.zavsmit.jokes.ui.common_jokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.fragment_jokes.*

abstract class JokesParentFragment : Fragment() {
    companion object {
        const val TEXT_SCREEN = 0
        const val DATA_SCREEN = 1
    }

    lateinit var viewAdapter: JokesAdapter
    protected var isMyJoke = false
    private lateinit var linLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_jokes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewAdapter = JokesAdapter(::onRightButtonClicked, ::onShareClicked, isMyJoke)
        linLayoutManager = LinearLayoutManager(requireContext())
        recyclerView = view.findViewById<RecyclerView>(R.id.rv_jokes).apply {
            setHasFixedSize(true)
            layoutManager = linLayoutManager
            adapter = viewAdapter
            addOnScrollListener(onScrollListener)
        }

        vmObserve()

        srl_list.apply {
            isEnabled = true
            setOnRefreshListener {
                refreshData()
            }
        }
    }

    override fun onDestroy() {
        onDestroyFragment()
        super.onDestroy()
    }

    protected fun toggleRefreshing(isRefreshing: Boolean) {
        srl_list.isRefreshing = isRefreshing
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        val PAGE_SIZE_DIVIDER = 5

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                val totalItemCount = linLayoutManager.itemCount
                val pastVisibleItems = linLayoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = linLayoutManager.childCount
                val lastVisibleItem = pastVisibleItems + visibleItemCount

                if (totalItemCount - lastVisibleItem <= PAGE_SIZE_DIVIDER) {
                    getNextData()
                }
            }
        }
    }

    protected fun showSnackBar(text: String) {
        view?.let { Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show() }
    }

    protected fun showScreen(screen: Int) {
        if (vf_list.displayedChild != screen) vf_list.displayedChild = screen
    }

    abstract fun refreshData()

    abstract fun initView()

    abstract fun vmObserve()

    abstract fun onRightButtonClicked(id: Long)

    abstract fun onShareClicked(text: String)

    abstract fun onDestroyFragment()

    abstract fun getNextData()
}
