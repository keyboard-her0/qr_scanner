package com.keyboardhero.qr.core.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.BaseButtomSheetLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var childFragment: Fragment? = null

    private var animationRes: Int? = null

    private lateinit var binding: BaseButtomSheetLayoutBinding

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        animationRes?.let {
            dialog?.window?.attributes?.windowAnimations = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BaseButtomSheetLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            if (this is BottomSheetDialog) {
                behavior.skipCollapsed = true
                behavior.state = STATE_EXPANDED
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragment?.also { fragment ->
            childFragmentManager.beginTransaction().apply {
                replace(R.id.bottomSheetHolder, fragment)
                commit()
            }
        }

        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        arguments?.getBoolean(KEY_HIDE_BOTTOM_TOGGLE_VIEW)?.let { isShow ->
            binding.bottomSheetToggleView.isVisible = isShow
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, null)
    }

    fun setDraggableState(isDraggable: Boolean) {
        (dialog as? BottomSheetDialog)?.let {
            it.behavior.isDraggable = isDraggable
        }
    }

    companion object {
        private const val KEY_HIDE_BOTTOM_TOGGLE_VIEW = "KEY_HIDE_BOTTOM_TOGGLE_VIEW"

        fun newInstance(childFragment: Fragment): BaseBottomSheetDialogFragment {
            return BaseBottomSheetDialogFragment().apply {
                this.childFragment = childFragment
            }
        }

        fun newInstance(childFragment: Fragment, animationRes: Int): BaseBottomSheetDialogFragment {
            return BaseBottomSheetDialogFragment().apply {
                this.childFragment = childFragment
                this.animationRes = animationRes
            }
        }

        fun newInstance(
            childFragment: Fragment,
            isHideBottomToggleView: Boolean? = false
        ): BaseBottomSheetDialogFragment {
            return BaseBottomSheetDialogFragment().apply {
                isHideBottomToggleView?.let {
                    arguments = Bundle().apply {
                        putBoolean(KEY_HIDE_BOTTOM_TOGGLE_VIEW, isHideBottomToggleView)
                    }
                }
                this.childFragment = childFragment
            }
        }
    }
}

fun Fragment.dismissBottomSheet() {
    (parentFragment as? BaseBottomSheetDialogFragment)?.dismiss()
}

fun Fragment.setCancelable(isCancelable: Boolean) {
    (parentFragment as? BaseBottomSheetDialogFragment)?.isCancelable = isCancelable
}
