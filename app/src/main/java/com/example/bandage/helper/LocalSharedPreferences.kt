package com.example.bandage.helper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
var token : String?=null
object LocalSharedPreferences {
    fun prefPutString(context: Context, key: String, value: String) {
        val pref =
            context.getSharedPreferences("bandage_prefs", AppCompatActivity.MODE_PRIVATE).edit()
        pref.putString(key, value)
        pref?.commit()
    }

    fun getPrefString(context: Context, key: String): String? {

        val pref =
            context.getSharedPreferences("bandage_prefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getString(key, "")
    }
    fun getToken(context: Context, key: String): String? {
        if(token!=null){
            return  token
        }
        token = this.getPrefString(context,key)
        return  token
    }

}