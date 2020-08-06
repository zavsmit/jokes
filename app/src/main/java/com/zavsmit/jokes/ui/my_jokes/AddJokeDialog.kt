package com.zavsmit.jokes.ui.my_jokes

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.dialog_add_joke.view.*

class AddJokeDialog : DialogFragment() {

    interface AddJokeDialogListener {
        fun setEtResultDialog(text: String)
    }

    private var listener: AddJokeDialogListener? = null

    companion object {
        fun newInstance() = AddJokeDialog()
        const val TAG = "AddJokeDialog"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (targetFragment != null && targetFragment is AddJokeDialogListener)
            targetFragment as AddJokeDialogListener
        else if (parentFragment != null && parentFragment is AddJokeDialogListener)
            parentFragment as AddJokeDialogListener
        else activity as? AddJokeDialogListener

        if (listener == null)
            throw ClassCastException("$context must implement AddJokeDialogListener")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_joke, null)
        builder.setTitle(R.string.add_new_joke)
                .setView(view)
                .setPositiveButton(R.string.save) { _, _ ->
                    val text = view.et_add_joke.text.toString()
                    listener?.setEtResultDialog(text)
                }
                .setNegativeButton(R.string.cancel, null)
        return builder.create()
    }

//    override fun onResume() {
//        super.onResume()
//
//        val etDialog = dialog as AlertDialog?
//        if (etDialog != null) {
//            val button = etDialog.getButton(Dialog.BUTTON_POSITIVE) as Button
//            button.setOnClickListener {
//                if (!isOnline(requireContext())) {
//                    Toast.makeText(activity, R.string.no_network, Toast.LENGTH_LONG).show()
//                    return@setOnClickListener
//                }
//
//                val result = etDialog.et_refuse_dialog.text.toString()
//                if (result.isBlank()) {
//                    etDialog.til_refuse_dialog.error = getString(dialogData.errorString)
//                } else {
//                    listener?.setEtResultDialog(result)
//                    etDialog.dismiss()
//                }
//            }
//        }
//    }
}