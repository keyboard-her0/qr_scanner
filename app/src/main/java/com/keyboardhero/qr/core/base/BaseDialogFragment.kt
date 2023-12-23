package com.keyboardhero.qr.core.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.keyboardhero.qr.R

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    protected val binding: VB by lazy { getViewBinding() }
    abstract fun getViewBinding(): VB

    protected open val isDismissWhenClickOutside: Boolean = false

    protected open val isDismissWhenPressBack: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(arguments)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), R.style.DialogStyle) {
            override fun onBackPressed() {
                if (isDismissWhenPressBack) dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        initAction()
    }

    protected open fun initData(arguments: Bundle?) {}
    protected open fun initView() {
        setupWindowDialog()
    }

    protected open fun initObserver() {

    }

    protected open fun initAction() {
        if (isDismissWhenClickOutside) {
            binding.root.setOnClickListener {
                if (isCancelable) {
                    dismiss()
                }
            }
        }
    }

    private fun setupWindowDialog() {
        dialog?.window?.run {
            setLayout(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
