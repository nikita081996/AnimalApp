package com.example.animalsapp.domain

import com.example.animalsapp.domain.model.CatModel
import io.reactivex.Single
import com.example.animalsapp.data.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by ankit.rajput on 09/12/20.
 */
interface CatRepository {
    suspend fun catList(): Flow<Result<List<CatModel>>>
}
