package com.muflihunnisa.githubapp2.view.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflihunnisa.githubapp2.domain.data.model.DetailUserResponse
import com.muflihunnisa.githubapp2.domain.repository.FavoriteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(private val favoriteRepo: FavoriteRepo) : ViewModel() {
    private val _favoriteLiveData : MutableLiveData<List<DetailUserResponse>?> = MutableLiveData()
    val favoriteLiveData get() = _favoriteLiveData

    private val _error : MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    init {
        getListDataFav()
    }

    private fun getListDataFav() {
        viewModelScope.launch {
            favoriteRepo.getListFav().collect {
                _favoriteLiveData.postValue(it)
            }
        }
    }
}