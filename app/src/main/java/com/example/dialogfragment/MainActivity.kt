package com.example.dialogfragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import com.example.dialogfragment.databinding.ActivityMainBinding
import com.example.dialogfragment.level1.SimpleDialogFragment
import com.example.dialogfragment.level1.SingleChoiceDialogFragment
import com.example.dialogfragment.level1.SingleChoiceWithConfirmationDialogFragment
import com.example.dialogfragment.level1.showToast
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var volume by notNull<Int>()

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
        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        setupSimpleDialogFragmentListener()
        setupSingleChoiceDialogFragmentListener()
        setupSingleChoiceWithConfirmationDialogFragment()
        updateUi()
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

    private fun setupSingleChoiceWithConfirmationDialogFragment() {
        SingleChoiceWithConfirmationDialogFragment.setupListener(supportFragmentManager, this) {
            this.volume = it
            updateUi()
        }
    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
//        binding.colorView.setBackgroundColor(color)
    }

    companion object {
        private const val KEY_VOLUME = "KEY_VOLUME"
    }
}