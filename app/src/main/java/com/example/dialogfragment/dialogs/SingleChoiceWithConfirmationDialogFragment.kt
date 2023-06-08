package com.example.dialogfragment.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.dialogfragment.entities.AvailableVolumeValues

class SingleChoiceWithConfirmationDialogFragment : DialogFragment() {
    private val volume: Int
        get() = requireArguments().getInt(ARG_VOLUME)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { "Volume: $it%" }
            .toTypedArray()

        return AlertDialog.Builder(requireContext())
            .setTitle("Volume setup")
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex, null)
            .setPositiveButton("Confirm") { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                val volume = volumeItems.values[index]
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(KEY_VOLUME_RESPONSE to volume)
                )
            }
            .create()
    }

    companion object {
        private val TAG = SingleChoiceDialogFragment::class.java.simpleName
        private val REQUEST_KEY = "$TAG:defaultRequestKey"
        private const val KEY_VOLUME_RESPONSE = "KEY_VOLUME_RESPONSE"
        private const val ARG_VOLUME = "ARG_VOLUME"

        fun show(manager: FragmentManager, volume: Int) {
            val dialogFragment = SingleChoiceWithConfirmationDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_VOLUME to volume)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Int) -> Unit
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener.invoke(result.getInt(KEY_VOLUME_RESPONSE))
                }
            )
        }
    }
}