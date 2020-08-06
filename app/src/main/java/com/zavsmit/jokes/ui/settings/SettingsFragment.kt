package com.zavsmit.jokes.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel.uiModel.observe(viewLifecycleOwner, Observer {
            s_offline.isChecked = it.isOffline
            et_first_name.setText(it.firstName)
            et_last_name.setText(it.lastName)
        })

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}