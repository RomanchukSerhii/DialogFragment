package com.example.dialogfragment

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import com.example.dialogfragment.databinding.ActivityMainBinding
import com.example.dialogfragment.level1.SimpleDialogFragment
import com.example.dialogfragment.level1.showToast

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            showSimpleDialogFragment()
        }
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
            })
    }
}