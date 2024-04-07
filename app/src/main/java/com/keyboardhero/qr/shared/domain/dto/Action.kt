package com.keyboardhero.qr.shared.domain.dto

import com.keyboardhero.qr.R

sealed class Action(
    val actionNameResId: Int,
    val iconRes: Int
) {
    object Copy : Action(actionNameResId = R.string.copy, R.drawable.ic_copy)
    object Share : Action(actionNameResId = R.string.share, R.drawable.ic_share)
    object Search : Action(actionNameResId = R.string.search, R.drawable.ic_search)
    object Connect : Action(actionNameResId = R.string.connect, R.drawable.ic_wifi)
    object Call : Action(actionNameResId = R.string.call, R.drawable.ic_call)
}