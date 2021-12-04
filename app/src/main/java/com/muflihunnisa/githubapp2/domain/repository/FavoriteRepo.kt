package com.muflihunnisa.githubapp2.domain.repository

import android.app.Application
import com.muflihunnisa.githubapp2.domain.data.local.Database
import com.muflihunnisa.githubapp2.domain.data.local.FavoriteDao
import com.muflihunnisa.githubapp2.domain.data.model.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepo @Inject constructor(application: Application) {
    private val favoriteDao : FavoriteDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = Database.database(application)
        favoriteDao = db.favoriteDao()
    }

    fun getListFav() : Flow<List<DetailUserResponse>>{
        return flow {
            val favList = favoriteDao.getFav()
            emit(favList)
        }.flowOn(Dispatchers.IO)
    }
    fun insertFav(userResponse: DetailUserResponse){
        executorService.execute { favoriteDao.insertFav(userResponse) }
    }

    fun deleteFav(userResponse: DetailUserResponse){
        executorService.execute { favoriteDao.delete(userResponse) }
    }
}