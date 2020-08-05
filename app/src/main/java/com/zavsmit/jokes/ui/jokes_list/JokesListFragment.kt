package com.zavsmit.jokes.ui.jokes_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.fragment_jokes_list.*

class JokesListFragment : Fragment() {

    private lateinit var jokesListViewModel: JokesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        jokesListViewModel =
                ViewModelProviders.of(this).get(JokesListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_jokes_list, container, false)
        jokesListViewModel.text.observe(viewLifecycleOwner, Observer {
            text_home.text = it
        })
        return root
    }
}