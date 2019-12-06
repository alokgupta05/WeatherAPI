package com.example.weatherapi.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



open class BaseViewModel : ViewModel() {

    var showLoading = MutableLiveData<String>()
    var dismissLoading = MutableLiveData<String>()

    protected fun onStartProgress() {
        showLoading.postValue("Fetching")
    }

    protected fun onEndProgress() {
        dismissLoading.postValue("")
    }


}
