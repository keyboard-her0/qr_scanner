package com.keyboardhero.qr.features

import android.util.Size
import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel<MainViewState, Unit>() {

    override fun initState(): MainViewState = MainViewState()


    fun generateQRBitmap(value : String, size: Size){
        viewModelScope.launch {

        }
    }
}
