package com.jetpackcomposemvvm.mvvm.model.repository

import com.jetpackcomposemvvm.mvvm.model.data.ImageData
import com.jetpackcomposemvvm.mvvm.model.interfaces.RetrofitInstance
import retrofit2.Response

class ImageDataRepository {

    private val apiService = RetrofitInstance.apiService

    suspend fun fetchImageData(): Response<List<ImageData>> {
        return apiService.getImageData()
    }
}