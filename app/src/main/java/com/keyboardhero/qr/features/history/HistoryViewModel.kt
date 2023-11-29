package com.keyboardhero.qr.features.history

import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() :
    BaseViewModel<HistoryViewState, HistoryViewEvents>() {
    override fun initState(): HistoryViewState = HistoryViewState(false)
}