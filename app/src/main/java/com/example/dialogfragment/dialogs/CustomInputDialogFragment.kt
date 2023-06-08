package com.example.dialogfragment.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner

typealias CustomInputDialogListener = (String, Int) -> Unit

class CustomInputDialogFragment : DialogFragment() {

    private val volume: Int
        get() = requireArguments().getInt(ARG_VOLUME)

    private val requestKey: String
        get() = requireArguments().getString(ARG_REQUEST_KEY) ?: ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        private val TAG = CustomInputDialogFragment::class.java.simpleName
        private const val ARG_VOLUME_RESPONSE = "ARG_VOLUME_RESPONSE"
        private const val ARG_VOLUME = "ARG_VOLUME"
        private const val ARG_REQUEST_KEY = "ARG_REQUEST_KEY"

        fun show(manager: FragmentManager, volume: Int, requestKey: String) {
            val dialogFragment = CustomInputDialogFragment()
            dialogFragment.arguments = bundleOf(
                ARG_VOLUME to volume,
                ARG_REQUEST_KEY to requestKey
            )
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            listener: CustomInputDialogListener
        ) {
            manager.setFragmentResultListener(
                requestKey,
                lifecycleOwner,
                FragmentResultListener { key, result ->
                    listener.invoke(key, result.getInt(ARG_VOLUME_RESPONSE))
                }
            )
        }
    }
}