package com.example.animalsapp.data

import com.example.animalsapp.domain.model.CatModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getCatList(
        @Url url: String
    ): Response<List<CatModel>>
}
