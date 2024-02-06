package com.example.animalsapp.domain

import com.example.animalsapp.domain.model.CatModel
import io.reactivex.Single
import com.example.animalsapp.data.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by ankit.rajput on 22,April,2021
 */
interface CatRepositoryUseCase {
    suspend fun execute(): Flow<Result<List<CatModel>>>
}

class CatRepositoryUseCaseImpl constructor(
    private val catRepository: CatRepository
) : CatRepositoryUseCase {

    override suspend fun execute(): Flow<Result<List<CatModel>>> =
        catRepository.catList()
}
