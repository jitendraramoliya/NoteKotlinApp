package com.noteapplication.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var pref = context.getSharedPreferences(Constant.Pref_Token_file, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        pref.edit().putString(Constant.User_Token, token).apply()
    }

    fun getToken(): String? {
        return pref.getString(Constant.User_Token, "")
    }

}