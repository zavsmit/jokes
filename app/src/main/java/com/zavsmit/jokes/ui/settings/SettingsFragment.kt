package com.zavsmit.jokes.ui.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_settings, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.uiModel.observe(viewLifecycleOwner, Observer {
            s_offline.isChecked = it.isOffline
            et_first_name.setText(it.firstName)
            et_last_name.setText(it.lastName)
        })

        settingsViewModel.getData()
    }

    override fun onStop() {
        super.onStop()

        val firstName = et_first_name.text.toString()
        val lastName = et_last_name.text.toString()
        val isOffline = s_offline.isChecked

        settingsViewModel.setData(firstName, lastName, isOffline)
        hideKeyboard(requireActivity())
    }

    private fun hideKeyboard(activity: Activity) {
        if (activity.currentFocus == null) return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}