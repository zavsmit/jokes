package com.zavsmit.jokes.ui.common_jokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zavsmit.jokes.R

abstract class JokesParentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: JokesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_jokes_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = JokesAdapter(::onLikeClicked, ::onShareClicked)
        recyclerView = view.findViewById<RecyclerView>(R.id.rv_jokes).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewAdapter
        }

        vmObserve()
    }

    abstract fun vmObserve()

    abstract fun onLikeClicked(id: Long)

    abstract fun onShareClicked(text: String)
}