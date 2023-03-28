package com.example.myapplication.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.TextView

class AppHelper
{
    companion object{
        fun isConnected(context: Context?): Boolean {
            return if (context == null) {
                false
            } else try {
                val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activeNetwork: NetworkInfo? = null
                if (cm != null) {
                    activeNetwork = cm.activeNetworkInfo
                }
                (activeNetwork != null
                        && activeNetwork.isConnectedOrConnecting)
            } catch (e: Exception) {
                true
            }
        }



    }

}