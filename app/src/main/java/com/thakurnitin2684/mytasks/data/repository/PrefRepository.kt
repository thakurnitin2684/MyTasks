package com.thakurnitin2684.mytasks.data.repository


import android.content.Context
import android.content.SharedPreferences


const val PREFERENCE_NAME = "MY_TASKS_REF"
const val IS_DARK_MODE_ON = "IS_DARK_MODE_ON"

class PrefRepository(val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val editor = pref.edit()





    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }


    private fun String.getBoolean(default: Boolean) = pref.getBoolean(this, default)

    fun setDarkBool(darkBool: Boolean) {
        IS_DARK_MODE_ON.put(darkBool)
    }

    fun getDarkBool(default: Boolean) = IS_DARK_MODE_ON.getBoolean(default)


    fun clearData() {
        editor.clear()
        editor.commit()
    }
}