package com.keyboardhero.qr.features.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentCreateBinding
import com.keyboardhero.qr.features.create.input.contact.InputContactScreen
import com.keyboardhero.qr.features.create.input.sms.InputSmsScreen
import com.keyboardhero.qr.features.create.input.url.InputUrlScreen
import com.keyboardhero.qr.features.create.input.wifi.InputWifiScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding
        get() = FragmentCreateBinding::inflate

    private val generateItemAdapter: CreateTypeItemAdapter by lazy { CreateTypeItemAdapter() }
    private val viewModel: CreateViewModel by viewModels()

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            rvItem.adapter = generateItemAdapter
            rvItem.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.create)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = { onBackPressed() }
        generateItemAdapter.onItemClick = {
            val screen = when (it.type) {
                BarcodeType.TEXT -> InputUrlScreen
                BarcodeType.PHONE -> InputUrlScreen
                BarcodeType.SMS -> InputSmsScreen
                BarcodeType.CONTACT -> InputContactScreen
                BarcodeType.WIFI -> InputWifiScreen
                BarcodeType.EMAIl -> InputWifiScreen
                BarcodeType.URL -> InputUrlScreen
            }
            router.navigate(
                screen
            )
        }
    }


    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.generateItem },
            observer = { generateItems -> generateItemAdapter.submitList(generateItems) }
        )
    }
}