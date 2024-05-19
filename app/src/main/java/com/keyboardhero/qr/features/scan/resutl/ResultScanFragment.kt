package com.keyboardhero.qr.features.scan.resutl

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.views.onSafeClick
import com.keyboardhero.qr.databinding.FragmentResultScanBinding
import com.keyboardhero.qr.shared.domain.dto.Action
import com.keyboardhero.qr.shared.domain.dto.barcodedata.AztecBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultScanFragment : BaseFragment<FragmentResultScanBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResultScanBinding
        get() = FragmentResultScanBinding::inflate

    private val args: ResultScanFragmentArgs by navArgs()

    private val viewModel: ResultScanViewModel by viewModels()

    override fun initData(data: Bundle?) {
        viewModel.iniData(args.scanData, args.isCreateNew, args.createAt)
    }

    override fun initViews() {
        //Do nothing
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(if (args.isCreateNew) R.string.result else R.string.detail)
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.barcodeData },
            observer = { barcodeData ->
                with(binding) {
                    imgType.setImageResource(viewModel.currentState.barcodeType.resIcon)
                    tvType.text = getString(viewModel.currentState.barcodeType.typeNameResId)
                    tvCreateAt.text = viewModel.currentState.createAt

                    when (barcodeData) {
                        is TextBarcode -> {
                            tvData.gravity = Gravity.CENTER
                            tvData.text = barcodeData.value
                        }

                        is UrlBarcode -> {
                            tvData.gravity = Gravity.CENTER
                            tvData.text = barcodeData.url
                        }

                        is PhoneBarcode -> {
                            tvData.gravity = Gravity.CENTER
                            tvData.text = barcodeData.phoneNumber
                        }

                        is SmsBarcode -> {
                            tvData.gravity = Gravity.START
                            tvData.text = getString(
                                R.string.result_sms,
                                barcodeData.phoneNumber,
                                barcodeData.message
                            )
                        }

                        is EmailBarcode -> {
                            tvData.gravity = Gravity.START
                            tvData.text = getString(
                                R.string.result_email,
                                barcodeData.email,
                                barcodeData.subject,
                                barcodeData.message
                            )
                        }

                        is WifiBarcode -> {
                            tvData.gravity = Gravity.START
                            val data = getString(
                                R.string.result_wifi,
                                barcodeData.ssid,
                                barcodeData.password,
                                barcodeData.type.typeName
                            )
                            val isHide =
                                if (barcodeData.isHide) getString(R.string.hide_network) else ""
                            tvData.text = "$data\n$isHide"
                        }
                    }
                }
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
                layoutParams.updateMargins(30, 4, 30, 4)

                actions.forEach { action ->

                    val drawable = ContextCompat.getDrawable(requireContext(), action.iconRes)
                    val paddingDrawable = resources.getDimensionPixelOffset(R.dimen.size_8dp)
                    val padding = resources.getDimensionPixelOffset(R.dimen.size_10dp)
                    val insetDrawable = InsetDrawable(drawable, 0, paddingDrawable, 0, 0)

                    val actionButton = TextView(requireContext()).apply {
                        setCompoundDrawablesWithIntrinsicBounds(null, insetDrawable, null, null)
                        text = getString(action.actionNameResId)
                        setPadding(padding, padding, padding, padding)
                        setBackgroundResource(R.drawable.background_generate_item)
                        gravity = Gravity.CENTER
                        minWidth = resources.getDimensionPixelOffset(R.dimen.size_80dp)
                        onSafeClick {
                            handleActionButtonClick(action = action)
                        }
                    }

                    val card = CardView(requireContext()).apply {
                        radius = resources.getDimensionPixelOffset(R.dimen.size_8dp).toFloat()
                    }
                    card.addView(actionButton)

                    binding.layoutAction.addView(card, layoutParams)
                }
            }
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun handleActionButtonClick(action: Action) {
        when (action) {
            is Action.Copy -> {
                val clipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", action.value)
                clipboardManager.setPrimaryClip(clipData)
            }

            is Action.Share -> {
                val intent = CommonUtils.createIntentShareStringData(
                    action.value,
                    getString(R.string.share_title)
                )
                startActivity(intent)
            }

            is Action.Call -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${action.phoneNumber}")
                startActivity(callIntent)
            }

            is Action.Open -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action.url))
                startActivity(intent)
            }

            is Action.SendSms -> {
                val smsIntent = Intent(Intent.ACTION_VIEW)
                smsIntent.data = Uri.parse("sms:${action.phoneNumber}")
                smsIntent.putExtra("sms_body", action.message)
                startActivity(smsIntent)
            }

            is Action.SendEmail -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(action.email))
                    putExtra(Intent.EXTRA_SUBJECT, action.subject)
                    putExtra(Intent.EXTRA_TEXT, action.message)
                }
                startActivity(intent)
            }

            else -> {
                //Do nothing
            }
        }
    }
}