package com.keyboardhero.qr

object NetworkConfig {
    const val CALL_TIMEOUT: Long = 40_000

    const val CONNECT_TIMEOUT: Long = 40_000

    const val READ_TIMEOUT: Long = 40_000

    const val WRITE_TIMEOUT: Long = 40_000

}

object SharedPreferenceConfig {
    const val SECURE_SHARED_PREFERENCES_FILE_NAME = "SECURE_PREFERENCE_FILE"

    const val SHARED_PREFERENCES_FILE_NAME = "PREFERENCE_FILE"
}

object Constant {

    const val EMPTY_STRING = ""

    const val SUFFIX_US = ".00"
    const val PRIVACY_URL = "https://sites.google.com/view/privacy-policy-scan-qr-code/"
}
