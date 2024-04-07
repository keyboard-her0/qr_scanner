package com.keyboardhero.qr.features.scan.resutl

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentResultScanBinding
import com.keyboardhero.qr.shared.domain.dto.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultScanFragment : BaseFragment<FragmentResultScanBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResultScanBinding
        get() = FragmentResultScanBinding::inflate

    private val args: ResultScanFragmentArgs by navArgs()

    private val viewModel: ResultScanViewModel by viewModels()

    override fun initData(data: Bundle?) {
        viewModel.iniData(args.scanData)
    }

    override fun initViews() {
        //Do nothing
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.result)
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    @SuppressLint("ResourceType")
    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.barcodeData },
            observer = {
                binding.tvData.text = it.getInputData()
            }
        )

        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.actions },
            observer = { actions ->
                binding.layoutAction.removeAllViews()
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.marginStart = 30
                layoutParams.marginEnd = 30

                actions.forEach { action ->

                    val drawable = ContextCompat.getDrawable(requireContext(), action.iconRes)
                    val paddingDrawable = resources.getDimensionPixelOffset(R.dimen.size_8dp)
                    val padding = resources.getDimensionPixelOffset(R.dimen.size_10dp)
                    val insetDrawable = InsetDrawable(drawable, 0, paddingDrawable, 0, 0)

                    val actionButton = TextView(requireContext()).apply {
                        setCompoundDrawablesWithIntrinsicBounds(null, insetDrawable, null, null)
                        text = getString(action.actionNameResId)
                        setPadding(padding, padding, padding, padding)
                        setBackgroundResource(R.drawable.background_border_16)
                        setOnClickListener {
                            handleActionButtonClick(action = action)
                        }
                        gravity = Gravity.CENTER
                        minWidth = resources.getDimensionPixelOffset(R.dimen.size_60dp)
                    }

                    binding.layoutAction.addView(actionButton, layoutParams)
                }
            }
        )
    }

    private fun handleActionButtonClick(action: Action) {
        when (action) {
            is Action.Copy -> {

            }

            is Action.Share -> {

            }

            is Action.Search -> {

            }

            is Action.Call -> {

            }

            is Action.Connect -> {

            }
        }
    }
}