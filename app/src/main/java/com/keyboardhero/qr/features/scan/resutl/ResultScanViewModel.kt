package com.keyboardhero.qr.features.scan.resutl

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.Action
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode
import com.keyboardhero.qr.shared.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ResultScanViewModel @Inject constructor(
    private val saveHistoryUseCase: SaveHistoryUseCase
) :
    BaseViewModel<ResultScanViewState, ResultScanViewEvents>() {
    override fun initState(): ResultScanViewState = ResultScanViewState()

    fun iniData(data: String, isCreateNew: Boolean) {
        viewModelScope.launch {
            val dataResult = handleData(data)
            if (isCreateNew) {
                val historyDTO = HistoryDTO(
                    id = 0,
                    isScan = true,
                    createAt = CommonUtils.getTimeNow(),
                    barcodeType = dataResult.first,
                    barcodeData = dataResult.second
                )
                saveHistory(historyDTO)
            }
            dispatchState(
                currentState.copy(
                    barcodeData = dataResult.second,
                    barcodeType = dataResult.first,
                    actions = dataResult.third
                )
            )
        }
    }

    private fun handleData(data: String): Triple<BarcodeType, BarcodeData, List<Action>> {
        val barcodeType: BarcodeType
        val barcodeData: BarcodeData
        val actions: List<Action>

        var regexPattern = Regex(REGEX_PHONE)
        var matchResult = regexPattern.find(data)
        if (matchResult != null && matchResult.groupValues.isNotEmpty()) {
            val phoneNumber = matchResult.groupValues[1]

            barcodeData = PhoneBarcode(phoneNumber = phoneNumber)
            barcodeType = BarcodeType.Phone
            actions = listOf(
                Action.Copy(value = phoneNumber),
                Action.Call(phoneNumber = phoneNumber),
                Action.Share(value = phoneNumber)
            )

            return Triple(barcodeType, barcodeData, actions)
        }

        regexPattern = Regex(REGEX_SMS, RegexOption.IGNORE_CASE)
        matchResult = regexPattern.find(data)
        if (matchResult != null && matchResult.groupValues.isNotEmpty()) {
            val phoneNumber = matchResult.groupValues[1]
            val message = matchResult.groupValues[2]

            barcodeData = SmsBarcode(phoneNumber = phoneNumber, message = message)
            barcodeType = BarcodeType.Sms
            actions = listOf(
                Action.Copy(value = phoneNumber + "\n" + message),
                Action.SendSms(phoneNumber, message),
                Action.Share(value = phoneNumber + message)
            )

            return Triple(barcodeType, barcodeData, actions)
        }

        regexPattern = Regex(REGEX_EMAIL)
        matchResult = regexPattern.find(data)
        if (matchResult != null && matchResult.groupValues.isNotEmpty()) {
            val email = matchResult.groupValues[1]
            val subject = matchResult.groupValues[2]
            val message = matchResult.groupValues[3]

            barcodeData = EmailBarcode(
                email = email, subject = subject, message = message
            )
            barcodeType = BarcodeType.Email
            actions = listOf(
                Action.Copy(email + "\n" + subject + "\n" + message),
                Action.SendEmail(email, subject, message),
            )

            return Triple(barcodeType, barcodeData, actions)
        }

        regexPattern = Regex(REGEX_URL)
        if (regexPattern.matches(data)) {
            barcodeData = UrlBarcode(url = data)
            barcodeType = BarcodeType.Url
            actions = listOf(
                Action.Copy(value = data),
                Action.Share(value = data),
                Action.Open(url = data),
            )
            return Triple(barcodeType, barcodeData, actions)
        }

        if (data.startsWith("WIFI:")) {
            val regex = Regex(REGEX_WIFI, RegexOption.IGNORE_CASE)
            val matches = regex.findAll(data)
            val values = mutableMapOf<String, String>()
            matches.forEach {match->
                val key = match.groupValues[1].uppercase(Locale.getDefault())
                val value = match.groupValues[2]
                values[key] = value
            }

            if (values.containsKey("S") && values.containsKey("T") && values.containsKey("P")) {
                barcodeData = WifiBarcode(
                    ssid = values["S"]!!,
                    type = WifiBarcode.TypeSecurity.getValue(values["T"]!!),
                    password = values["P"]!!,
                    isHide = values["H"] == "true"
                )
                barcodeType = BarcodeType.Wifi
                actions = listOf(
                    Action.Copy(
                        barcodeData.ssid + "\n" + barcodeData.password + "\n" + barcodeData.type
                    ),
                )
                return Triple(barcodeType, barcodeData, actions)
            }
        }

        barcodeData = TextBarcode(value = data)
        barcodeType = BarcodeType.Text
        actions = listOf(
            Action.Copy(value = data),
            Action.Share(value = data),
        )
        return Triple(barcodeType, barcodeData, actions)
    }

    private suspend fun saveHistory(historyDTO: HistoryDTO) {
        saveHistoryUseCase.invoke(historyDTO)
    }

    companion object {
        private const val REGEX_WIFI = "([STPH]):([^;]+);"
        private const val REGEX_PHONE = "tel:(\\+?[0-9]+)"
        private const val REGEX_SMS = "SMSTO:(.*?):(.*?)\$"
        private const val REGEX_EMAIL = "MATMSG:TO:(.*?);SUB:(.*?);BODY:(.*?);"
        private const val REGEX_URL =
            "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9]+)*([/?#]\\S*)?\$"
    }
}