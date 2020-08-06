package com.zavsmit.jokes.ui.my_jokes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zavsmit.jokes.R
import com.zavsmit.jokes.ui.jokes_list.JokesAdapter
import kotlinx.android.synthetic.main.fragment_my_jokes.view.*

class MyJokesFragment : Fragment(), AddJokeDialog.AddJokeDialogListener {

    private val myJokesViewModel: MyJokesViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: JokesAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_jokes, container, false)

        viewAdapter = JokesAdapter()
        recyclerView = root.findViewById<RecyclerView>(R.id.rv_my_jokes).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = viewAdapter
        }

        myJokesViewModel.uiJoke.observe(viewLifecycleOwner, Observer {
            viewAdapter.setData(it)
        })

        root.fab_add.setOnClickListener { AddJokeDialog.newInstance().show(childFragmentManager, AddJokeDialog.TAG) }

        return root
    }

    override fun setEtResultDialog(text: String) {

    }
}