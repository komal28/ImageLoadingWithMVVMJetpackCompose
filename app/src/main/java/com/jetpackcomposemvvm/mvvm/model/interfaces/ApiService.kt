package com.jetpackcomposemvvm.mvvm.model.interfaces

import com.jetpackcomposemvvm.mvvm.model.data.ImageData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("content/misc/media-coverages?limit=100")
    suspend fun getImageData(): Response<List<ImageData>>
}

