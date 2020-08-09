package com.zavsmit.jokes.ui.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zavsmit.jokes.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_settings, container, false)

    private var timer = Timer()
    private val TIMER_DELAY: Long = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        s_offline.setOnCheckedChangeListener { _, isCheck -> settingsViewModel.setOfflineMode(isCheck) }
        et_first_name.doOnTextChanged { text, _, _, _ -> saveWithDelay(text.toString(), ::saveFirstName) }
        et_last_name.doOnTextChanged { text, _, _, _ -> saveWithDelay(text.toString(), ::saveLastName) }

        settingsViewModel.uiModel.observe(viewLifecycleOwner, Observer {
            s_offline.isChecked = it.isOffline
            et_first_name.setText(it.firstName)
            et_last_name.setText(it.lastName)
        })

        settingsViewModel.getData()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())
    }

    private fun hideKeyboard(activity: Activity) {
        if (activity.currentFocus == null) return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    private fun saveWithDelay(text: String, saveText: (m: String) -> Unit) {
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                saveText(text)
            }
        }, TIMER_DELAY)
    }

    private fun saveFirstName(text: String) {
        settingsViewModel.saveFirstName(text)
    }

    private fun saveLastName(text: String) {
        settingsViewModel.saveLastName(text)
    }
}