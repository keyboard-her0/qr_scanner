package com.keyboardhero.qr.features

import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.qr.QRUtils
import com.keyboardhero.qr.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {

    }

    override fun initActions() {
        binding.btnScanNow.setOnClickListener {
            if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                val action = MainFragmentDirections.actionMainScreenToScannerFragment()
                findNavController().navigate(action)
            } else {
                showSingleOptionDialog(
                    title = "Lỗi",
                    message = "Không hỗ trợ",
                    button = "Đóng"
                )
            }
        }

        binding.btnMyQr.setOnClickListener {
            lifecycleScope.launch {
                val bitmap = QRUtils.generateQRBitmap("123456", Size(300, 300))
                if (bitmap != null){
                    binding.imgResult.setImageBitmap(bitmap)
                }
            }
        }
    }

//    private fun connectToWifi(ssid: String, password: String) {
//        val specifier = WifiNetworkSpecifier.Builder()
//            .setSsid(ssid)
//            .setWpa2Passphrase(password)
//            .build()
//
//
//        val request = NetworkRequest.Builder()
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .setNetworkSpecifier(specifier)
//            .build()
//
//        val connectivityManager: ConnectivityManager = requireContext().getSystemService(
//            ConnectivityManager::class.java
//        )
//        connectivityManager.requestNetwork(request, object : NetworkCallback() {
//            override fun onAvailable(network: Network) {
//            }
//
//            override fun onUnavailable() {
//
//            }
//        })
//    }

    override fun initObservers() {
    }
}
