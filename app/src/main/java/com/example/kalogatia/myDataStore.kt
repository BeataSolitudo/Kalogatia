package com.example.kalogatia

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_theme")

object DataStoreManager {
    private val KEY_THEME = stringPreferencesKey("theme")

    suspend fun saveTheme(context: Context, theme: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME] = theme
        }
    }

    fun getTheme(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[KEY_THEME] ?: "colorful"
        }
    }
}