package com.example.dialogfragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import com.example.dialogfragment.databinding.ActivityMainBinding
import com.example.dialogfragment.dialogs.CustomInputDialogFragment
import com.example.dialogfragment.dialogs.CustomInputDialogListener
import com.example.dialogfragment.dialogs.MultipleChoiceDialogFragment
import com.example.dialogfragment.dialogs.SimpleDialogFragment
import com.example.dialogfragment.dialogs.SingleChoiceDialogFragment
import com.example.dialogfragment.dialogs.SingleChoiceWithConfirmationDialogFragment
import com.example.dialogfragment.dialogs.showToast
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var volume by notNull<Int>()
    private var currentVolume1 by notNull<Int>()
    private var currentVolume2 by notNull<Int>()
    private var color by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonDefault.setOnClickListener {
            showSimpleDialogFragment()
        }
        binding.buttonSingleChoice.setOnClickListener {
            showSingleChoiceDialogFragment()
        }
        binding.buttonSingleChoiceWithConfirm.setOnClickListener {
            showSingleChoiceWithConfirmationDialogFragment()
        }
        binding.buttonMultipleChoice.setOnClickListener {
            showMultipleChoiceDialogFragment()
        }
        binding.buttonInputAndValidation.setOnClickListener {
            showCustomInputDialogFragment(KEY_FIRST_REQUEST_KEY, currentVolume1)
        }
        binding.buttonInputAndValidation2.setOnClickListener {
            showCustomInputDialogFragment(KEY_SECOND_REQUEST_KEY, currentVolume2)
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        currentVolume1 = savedInstanceState?.getInt(KEY_FIRST_REQUEST_KEY) ?: 50
        currentVolume2 = savedInstanceState?.getInt(KEY_SECOND_REQUEST_KEY) ?: 50
        color = savedInstanceState?.getInt(KEY_COLOR) ?: Color.RED
        updateUi()

        setupSimpleDialogFragmentListener()
        setupSingleChoiceDialogFragmentListener()
        setupSingleChoiceWithConfirmationDialogFragmentListener()
        setupMultipleChoiceDialogFragmentListener()
        setupCustomInputDialogFragmentListener()
    }

    private fun showSimpleDialogFragment() {
        val dialogFragment = SimpleDialogFragment()
        dialogFragment.show(supportFragmentManager, SimpleDialogFragment.TAG)
    }

    private fun setupSimpleDialogFragmentListener() {
        supportFragmentManager.setFragmentResultListener(
            SimpleDialogFragment.REQUEST_KEY,
            this,
            FragmentResultListener { _, result ->
                val which = result.getInt(SimpleDialogFragment.KEY_RESPONSE)
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> showToast(R.string.uninstall_confirmed)
                    DialogInterface.BUTTON_NEGATIVE -> showToast(R.string.uninstall_rejected)
                    DialogInterface.BUTTON_NEUTRAL -> showToast(R.string.uninstall_ignored)
                }
            }
        )
    }

    private fun showSingleChoiceDialogFragment() {
        SingleChoiceDialogFragment.show(supportFragmentManager, volume)
    }

    private fun setupSingleChoiceDialogFragmentListener() {
        SingleChoiceDialogFragment.setupListener(supportFragmentManager, this) {
            this.volume = it
            updateUi()
        }
    }

    private fun showSingleChoiceWithConfirmationDialogFragment() {
        SingleChoiceWithConfirmationDialogFragment.show(supportFragmentManager, volume)
    }

    private fun setupSingleChoiceWithConfirmationDialogFragmentListener() {
        SingleChoiceWithConfirmationDialogFragment.setupListener(supportFragmentManager, this) {
            this.volume = it
            updateUi()
        }
    }

    private fun showMultipleChoiceDialogFragment() {
        MultipleChoiceDialogFragment.show(supportFragmentManager, color)
    }

    private fun setupMultipleChoiceDialogFragmentListener() {
        MultipleChoiceDialogFragment.setupListener(supportFragmentManager, this) {
            this.color = it
            updateUi()
        }
    }

    private fun showCustomInputDialogFragment(requestKey: String, volume: Int) {
        CustomInputDialogFragment.show(supportFragmentManager, volume, requestKey)
    }

    private fun setupCustomInputDialogFragmentListener() {
        val listener: CustomInputDialogListener = { requestKey, volume ->
            when (requestKey) {
                KEY_FIRST_REQUEST_KEY -> this.currentVolume1 = volume
                KEY_SECOND_REQUEST_KEY -> this.currentVolume2 = volume
            }
            updateUi()
        }
        CustomInputDialogFragment.setupListener(
            supportFragmentManager,
            this,
            KEY_FIRST_REQUEST_KEY,
            listener
        )
        CustomInputDialogFragment.setupListener(
            supportFragmentManager,
            this,
            KEY_SECOND_REQUEST_KEY,
            listener
        )
    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
        binding.colorView.setBackgroundColor(color)
        binding.tvCurrentVolume1.text = getString(R.string.current_volume_1, currentVolume1)
        binding.tvCurrentVolume2.text = getString(R.string.current_volume_2, currentVolume2)
    }

    companion object {
        private const val KEY_VOLUME = "KEY_VOLUME"
        private const val KEY_COLOR = "KEY_COLOR"
        private const val KEY_FIRST_REQUEST_KEY = "KEY_FIRST_REQUEST_KEY"
        private const val KEY_SECOND_REQUEST_KEY = "KEY_SECOND_REQUEST_KEY"
    }
}