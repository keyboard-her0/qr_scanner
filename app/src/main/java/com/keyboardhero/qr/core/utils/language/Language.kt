package com.keyboardhero.qr.core.utils.language

class Language(id: Int, name: String, code: String) {
    private var _id = id
    private var _name: String? = name
    private var _code: String? = code

    fun getId(): Int {
        return _id
    }

    fun getName(): String? {
        return _name
    }

    fun getCode(): String? {
        return _code
    }
}
