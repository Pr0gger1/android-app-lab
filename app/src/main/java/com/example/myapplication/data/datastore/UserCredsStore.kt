package com.example.myapplication.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserDataStoreKeys {
    const val STORE_NAME = "userCreds"
    const val EMAIL_KEY = "email"
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UserDataStoreKeys.STORE_NAME
)


interface UserDataStoreState {
    val email: Preferences.Key<String>
}

data class UserPreferences(
    var email: String?
)

class UserDataStore(private val context: Context) {
    val preferenceKeys = object : UserDataStoreState {
        override var email: Preferences.Key<String> =
            stringPreferencesKey(UserDataStoreKeys.EMAIL_KEY)
    }

    suspend fun updateCreds(preferences: UserPreferences?) {
        context.dataStore.edit { pref ->
            pref[preferenceKeys.email] = preferences?.email ?: ""
        }
    }

    fun getCreds(): Flow<UserPreferences> = context.dataStore.data.map { pref ->
        UserPreferences(email = pref[preferenceKeys.email])
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}