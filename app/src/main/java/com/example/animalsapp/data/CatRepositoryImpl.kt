package com.example.animalsapp.data

import com.example.animalsapp.domain.CatRepository
import com.example.animalsapp.domain.model.CatModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response

class CatRepositoryImpl constructor(
    private val apiService: ApiService,
) : CatRepository {

    override suspend fun catList(): Flow<Result<List<CatModel>>> {
        val url = "https://demo3398465.mockable.io/v1/images/search"

        return makeFlowApiCall {
            apiService.getCatList(url)
        }.convertResponseToResult { it }
    }
}

fun <T : Any, R : Any> Flow<Response<T>>.convertResponseToResult(transform: Transform<T, R>): Flow<Result<R>> {
    return this.map { FlowResponseMapper(transform = transform).invoke(response = it) }
        .catch {
            emit(value = Result.Error(meta = it.message, throwable = it))
        }
}

suspend fun <T> makeFlowApiCall(block: suspend () -> Response<T>) =
    flow {
        val data = block.invoke()
        emit(data)
    }.flowOn(Dispatchers.IO)