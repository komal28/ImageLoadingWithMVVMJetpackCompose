package com.jetpackcomposemvvm.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcomposemvvm.mvvm.model.data.ImageData
import com.jetpackcomposemvvm.mvvm.model.repository.ImageDataRepository
import kotlinx.coroutines.launch
class ImageDataViewModel : ViewModel() {
    private val repository = ImageDataRepository()
    private val _imageData = MutableLiveData<List<ImageData>>()
    val imageData: LiveData<List<ImageData>> = _imageData

    fun fetchMediaCoverages() {
        viewModelScope.launch {
            try {
                val response = repository.fetchImageData()
                if (response.isSuccessful) {
                    _imageData.postValue(response.body())
                } else {
                    //
                }
            } catch (e: Exception) {
                Log.e("ImageList Failed : ", e.toString())
            }
        }
    }
}