package com.keyboardhero.qr.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.keyboardhero.qr.core.router.Router
import com.keyboardhero.qr.databinding.FragmentHistoryListBinding
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.features.scan.resutl.ResultScanFragmentArgs
import com.keyboardhero.qr.features.scan.resutl.ResultScanScreen
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryListFragment : Fragment() {
    private lateinit var binding: FragmentHistoryListBinding
    private var viewModel: HistoryViewModel? = null
    private var isScan = false
    private val historyAdapter by lazy { HistoryAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initActions()
        initObservers()
    }

    private fun initData() {
        (parentFragment as? HistoryFragment)?.let { historyFragment ->
            viewModel = historyFragment.getHistoryViewModel()
        }
    }

    private fun initViews() {
        with(binding) {
            rvHistory.adapter = historyAdapter
            rvHistory.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @Inject
    lateinit var router: Router

    private fun initActions() {
        historyAdapter.listener = object : HistoryAdapter.HistoryListener {
            override fun onItemClick(history: HistoryDTO) {
                if (isScan) {
                    router.navigate(
                        ResultScanScreen,
                        ResultScanFragmentArgs(
                            scanData = history.barcodeData.getInputData(),
                            isCreateNew = false
                        ).toBundle()
                    )
                } else {
                    navigateToCreateResultScreen(history)
                }
            }
        }
    }

    private fun navigateToCreateResultScreen(history: HistoryDTO) {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = history.barcodeData,
                type = history.barcodeType,
                isCreateNew = false
            ).toBundle()
        )
    }

    private fun initObservers() {
        viewModel?.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.listHistory },
            observer = { listHistory ->
                historyAdapter.submitList(listHistory.filter { it.isScan == isScan })
            }
        )
    }

    companion object {
        fun newInstance(
            scan: Boolean
        ): HistoryListFragment {
            return HistoryListFragment().apply { isScan = scan }
        }
    }
}