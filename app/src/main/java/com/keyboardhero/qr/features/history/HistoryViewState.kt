package com.keyboardhero.qr.features.history

import com.keyboardhero.qr.shared.domain.dto.HistoryDTO

data class HistoryViewState(
    val loading: Boolean,
    val listHistory: List<HistoryDTO>
)