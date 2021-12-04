package com.muflihunnisa.githubapp2.view.detail.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflihunnisa.githubapp2.domain.data.model.DetailUserResponse
import com.muflihunnisa.githubapp2.domain.data.network.ApiResult
import com.muflihunnisa.githubapp2.domain.repository.FavoriteRepo
import com.muflihunnisa.githubapp2.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userRepository: UserRepository, private val favoriteRepo: FavoriteRepo) : ViewModel() {
    private val _detailUser : MutableLiveData<DetailUserResponse?> = MutableLiveData()
    val detailUser get() = _detailUser

    private val _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error :MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    private var strUsername : String = " "

    private val _isFav : MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite get() = _isFav

    private val _listFavorite: MutableLiveData<List<DetailUserResponse>?> = MutableLiveData()
    init {
        getFavUser()
        _isFav.postValue(false)
    }

    fun getDetailUser(username : String){
        if (strUsername != username){
            viewModelScope.launch {
                strUsername = username
                userRepository.getDetailUser(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when(it){
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _detailUser.postValue(it.data)
                        }
                        is ApiResult.Error -> {
                            _error.postValue(it.throwable)
                        }
                    }
                }
            }
        }
    }
    private fun insert(favUser : DetailUserResponse?){
        viewModelScope.launch {
            if (favUser != null){
                favoriteRepo.insertFav(favUser)
                getFavUser()
                _isFav.postValue(true)
            }
        }
    }

    private fun getFavUser() {
        viewModelScope.launch {
            favoriteRepo.getListFav().collect {
                _listFavorite.postValue(it)
            }
        }
    }

    fun showFavorite(favUser: DetailUserResponse?){
        viewModelScope.launch {
            for (it in _listFavorite.value ?: mutableListOf()){
                if (favUser?.login == it.login){
                    _isFav.postValue(true)
                    break
                }else{
                    _isFav.postValue(false)
                }
            }
        }
    }

    private fun delete (favUser: DetailUserResponse?){
        viewModelScope.launch {
            if (favUser != null){
                favoriteRepo.deleteFav(favUser)
                getFavUser()
                _isFav.postValue(false)
            }
        }
    }

    fun isFavoriteUser (favUser: DetailUserResponse?){
        viewModelScope.launch {
            if (_isFav.value == true){
                delete(favUser)
            }else{
                insert(favUser)
            }
        }
    }
}