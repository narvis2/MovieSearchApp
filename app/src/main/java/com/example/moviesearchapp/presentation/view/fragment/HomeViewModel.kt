package com.example.moviesearchapp.presentation.view.fragment

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesearchapp.domain.model.MovieInfoModel
import com.example.moviesearchapp.domain.usecase.GetMovieListPagingDataUseCase
import com.example.moviesearchapp.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieListPagingDataUseCase: GetMovieListPagingDataUseCase
) : BaseViewModel() {

    val loadingType = MutableStateFlow(MovieLoadingType.LOADING)

    val searchQuery = MutableStateFlow("")

    val getMovieList: Flow<PagingData<MovieInfoModel>> =
        getMovieListPagingDataUseCase(searchQuery).cachedIn(viewModelScope)

}