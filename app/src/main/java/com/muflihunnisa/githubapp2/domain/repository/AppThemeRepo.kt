package com.muflihunnisa.githubapp2.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppThemeRepo @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore by preferencesDataStore("Preference")
    private val keyPreference = booleanPreferencesKey("theme_preference")
    private val preferenceDataStore = context.dataStore

    fun getThemePreference(): Flow<Boolean>{
        return preferenceDataStore.data.map {
            it[keyPreference] ?: false
        }
    }

    suspend fun saveThemePreference(isDark : Boolean){
        preferenceDataStore.edit {
            it[keyPreference] = isDark
        }
    }
}