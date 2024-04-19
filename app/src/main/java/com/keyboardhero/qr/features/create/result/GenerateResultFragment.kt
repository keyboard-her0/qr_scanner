package com.keyboardhero.qr.features.create.result

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.views.onSafeClick
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import com.keyboardhero.qr.shared.domain.dto.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    private val args: GenerateResultFragmentArgs by navArgs()
    private val viewModel: GenerateResultViewModel by viewModels()
    private var isCreateNew: Boolean = true

    override fun initData(data: Bundle?) {
        isCreateNew = args.isCreateNew
        viewModel.setup(
            type = args.type,
            barcodeData = args.barcodeData,
            isCreateNew = isCreateNew
        )
    }

    override fun initViews() {
    }

    private fun handleActionButtonClick(action: Action) {
        when (action) {
            is Action.Save -> handleSaveImage(action.bitmap)
            is Action.ShareImage -> handleShareQr(action.bitmap)
            else -> {
                //DO nothing
            }
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(
            if (isCreateNew) R.string.title_generate_result_screen else R.string.detail
        )
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    private fun handleSaveImage(bitmap: Bitmap) {
        val permissionImage = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        requestPermissions(permissionImage) { allow, _ ->
            if (allow) {
                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        CommonUtils.saveImage(bitmap, requireContext())
                    }
                    Toast.makeText(
                        requireContext(),
                        if (result) "Lưu ảnh thành công" else "Lưu ảnh thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handleShareQr(bitmap: Bitmap) {
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            "temp-file_qr",
            null
        )
        if (path != null) {
            val uri = Uri.parse(path)
            val intent = CommonUtils.createIntentShareUriData(uri, "Chia sẻ với")
            startActivity(intent)
        }
    }

    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.bitmap },
            observer = { bitmap ->
                Glide.with(requireContext()).load(bitmap).into(binding.imgQr)
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
                layoutParams.updateMargins(
                    resources.getDimensionPixelOffset(R.dimen.size_35dp),
                    resources.getDimensionPixelOffset(R.dimen.size_2dp),
                    resources.getDimensionPixelOffset(R.dimen.size_35dp),
                    resources.getDimensionPixelOffset(R.dimen.size_2dp),
                )

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
}