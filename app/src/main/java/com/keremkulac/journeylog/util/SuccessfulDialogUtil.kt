package com.keremkulac.journeylog.util

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.keremkulac.journeylog.databinding.DialogSuccessfulBinding

class SuccessfulDialogUtil(
    private val context: Context,
    private val message: String
) {
    private lateinit var binding: DialogSuccessfulBinding
    private val dialog: Dialog = createDialog()

    private fun createDialog(): Dialog {
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            binding = DialogSuccessfulBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupWindow()
        }
    }

    private fun Dialog.setupWindow() {
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            decorView.setPadding(64, 64, 64, 64)
        }
    }

    fun showDialog() {
        with(binding) {
            successAnimation.playAnimation()
            successMessage.text = message
            dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)
        }
    }

}
