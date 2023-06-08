package com.example.dialogfragment.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.dialogfragment.R

class MultipleChoiceDialogFragment : DialogFragment() {

    private val color: Int
        get() = requireArguments().getInt(ARG_COLOR)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val colorItems = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkboxes = colorComponents
            .map { it > 0 && savedInstanceState == null }
            .toBooleanArray()
        return AlertDialog.Builder(requireContext())
            .setTitle("Volume setup")
            .setMultiChoiceItems(colorItems, checkboxes) { dialog, which, isCheked ->
                val checkedPositions = (dialog as AlertDialog).listView.checkedItemPositions
                val color = Color.rgb(
                    booleanToColorComponent(checkedPositions[0]),
                    booleanToColorComponent(checkedPositions[1]),
                    booleanToColorComponent(checkedPositions[2]),
                )
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(KEY_COLOR_RESPONSE to color)
                )
            }
            .setPositiveButton("Close", null)
            .create()
    }

    private fun booleanToColorComponent(value: Boolean): Int {
        return if (value) 255 else 0
    }

    companion object {
        private val TAG = MultipleChoiceDialogFragment::class.java.simpleName
        private val REQUEST_KEY = "$TAG:defaultResponseKey"
        private const val KEY_COLOR_RESPONSE = "COLOR_RESPONSE"
        private const val ARG_COLOR = "ARG_COLOR"

        fun show(manager: FragmentManager, color: Int) {
            val dialogFragment = MultipleChoiceDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_COLOR to color)
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
                    listener.invoke(result.getInt(KEY_COLOR_RESPONSE))
                }
            )
        }
    }
}