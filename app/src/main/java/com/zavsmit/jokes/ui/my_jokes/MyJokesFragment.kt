package com.zavsmit.jokes.ui.my_jokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.fragment_my_jokes.*
import kotlinx.android.synthetic.main.fragment_my_jokes.view.*

class MyJokesFragment : Fragment() {

    private lateinit var myJokesViewModel: MyJokesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myJokesViewModel =
            ViewModelProviders.of(this).get(MyJokesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_jokes, container, false)
        myJokesViewModel.text.observe(viewLifecycleOwner, Observer {
            text_gallery.text = it
        })

        root.fab_add.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        return root
    }
}