package com.keyboardhero.qr.features.scan.resutl

import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.Action
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode

data class ResultScanViewState(
    val loading: Boolean = false,
    val barcodeData: BarcodeData = TextBarcode(""),
    val barcodeType: BarcodeType = BarcodeType.Text,
    val createAt: String = CommonUtils.getTimeNow(),
    val actions: List<Action> = emptyList()
)
