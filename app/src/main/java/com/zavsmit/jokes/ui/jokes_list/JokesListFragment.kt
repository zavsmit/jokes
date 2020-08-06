package com.zavsmit.jokes.ui.jokes_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zavsmit.jokes.R

class JokesListFragment : Fragment() {

    private val jokesListViewModel: JokesListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: JokesAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_jokes_list, container, false)

        viewAdapter = JokesAdapter()
        recyclerView = root.findViewById<RecyclerView>(R.id.rv_jokes).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewAdapter
        }

        jokesListViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.setData(it)
        })
        return root
    }
}