package com.example.moviesearchapp.view.fragment.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.MovieInfoModel
import com.example.domain.usecase.GetMovieListPagingDataUseCase
import com.example.moviesearchapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieListPagingDataUseCase: GetMovieListPagingDataUseCase
) : BaseViewModel() {

    val loadingType = MutableStateFlow(MovieLoadingType.LOADING)

    val searchQuery = MutableStateFlow("")

    val searchFocusView = MutableStateFlow(false)

    val notBlankContents = MutableStateFlow(false)

    val getMovieList: Flow<PagingData<MovieInfoModel>> =
        getMovieListPagingDataUseCase(searchQuery).cachedIn(viewModelScope)

    private val _scrollTop = Channel<Unit>(Channel.CONFLATED)
    val scrollTop = _scrollTop.receiveAsFlow()

    private val _refresh = Channel<Unit>(Channel.CONFLATED)
    val refresh = _refresh.receiveAsFlow()

    private val _search = Channel<Unit>(Channel.CONFLATED)
    val search = _search.receiveAsFlow()

    // Click Listener
    fun onScrollTop() = viewModelScope.launch {
        _scrollTop.send(Unit)
    }

    fun onRefreshList() = viewModelScope.launch {
        _refresh.send(Unit)
    }

    fun onSearchClick() = viewModelScope.launch {
        hideKeyboard()
        if (searchQuery.value.isBlank()) {
            showSnackBar("영화 제목을 검색해 주세요.", 50)
            return@launch
        }

        _search.send(Unit)
    }

    fun onClearEdit() {
        searchQuery.value = ""
    }
}