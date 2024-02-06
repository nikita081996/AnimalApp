package com.example.animalsapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalsapp.data.ApiService
import com.example.animalsapp.data.CatRepositoryImpl
import com.example.animalsapp.data.RetrofitHelper
import com.example.animalsapp.domain.CatRepository
import com.example.animalsapp.domain.CatRepositoryUseCase
import com.example.animalsapp.domain.CatRepositoryUseCaseImpl
import kotlinx.coroutines.launch
import com.example.animalsapp.data.Result
import com.example.animalsapp.domain.model.CatModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {
    private var catRepositoryUseCase: CatRepositoryUseCase? = null
    private var catRepository: CatRepository? = null
    private var catList = mutableStateListOf<CatModel>()
    var animalState = MutableStateFlow<Result<List<CatModel>>>(Result.Loading)
    init {
        catRepository = CatRepositoryImpl(
            RetrofitHelper.getInstance().create<ApiService>(ApiService::class.java)
        )
        catRepositoryUseCase = CatRepositoryUseCaseImpl(catRepository as CatRepositoryImpl)
        getCatList()
    }

    private fun getCatList() {
        viewModelScope.launch {
            catRepositoryUseCase?.execute()
                ?.collect {
                    when (it) {
                        is Result.Success -> {
                            Log.d("API_CALL Success", it.data?.size.toString())
                            animalState.value = Result.Success(it.data)
                        }

                        is Result.Error -> {
                            Log.d("API_CALL Failure", it.throwable.toString())
                            animalState.value = Result.Error(it.meta, it.throwable)
                        }
                        is Result.Loading -> {
                            Log.d("API_CALL Inprogress", it.toString())
                            animalState.value = Result.Loading
                        }

                        else -> {}
                    }
                }
        }
    }
}