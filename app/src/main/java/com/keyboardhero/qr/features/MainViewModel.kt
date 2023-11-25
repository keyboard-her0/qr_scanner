package com.keyboardhero.qr.features

import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel<MainViewState, Unit>() {

    override fun initState(): MainViewState = MainViewState()

}
