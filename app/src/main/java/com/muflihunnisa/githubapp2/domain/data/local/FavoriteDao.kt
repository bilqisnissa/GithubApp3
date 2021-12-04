package com.muflihunnisa.githubapp2.domain.data.local

import androidx.room.*
import com.muflihunnisa.githubapp2.domain.data.model.DetailUserResponse

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav (detailUserResponse: DetailUserResponse)

    @Query("SELECT * from detailUserResponse")
    fun getFav() :List<DetailUserResponse>

    @Delete
    fun delete(detailUserResponse: DetailUserResponse)
}