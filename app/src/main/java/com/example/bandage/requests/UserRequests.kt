package com.example.bandage.requests

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bandage.activities.HomeActivity
import com.example.bandage.activities.AuthActivity
import com.example.bandage.activities.ProfileActivity
import com.example.bandage.api.ApiInterface
import com.example.bandage.api.RetrofitClient
import com.example.bandage.helper.LocalSharedPreferences
import com.example.bandage.helper.token
import com.example.bandage.models.request_model.LoginRegisterRequest
import com.example.bandage.models.response_model.LoginRegisterResponse
import com.example.bandage.models.response_model.UserResponse
import com.example.myapplication.helper.AppHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRequests(val context: Context) {


    private val apiInterface: ApiInterface =
        RetrofitClient.getInstance().create(ApiInterface::class.java)


    fun registerUser(
        newUserData: LoginRegisterRequest,
        btn: TextView,
        progress: ProgressBar,
        isLoginSaved: Boolean = true
    ) {
        loginSignupUser(btn, progress, isLoginSaved, apiInterface.registerUser(newUserData))
    }

    fun loginUser(
        newUserData: LoginRegisterRequest,
        btn: TextView,
        progress: ProgressBar,
        isLoginSaved: Boolean = false
    ) {
        loginSignupUser(btn, progress, isLoginSaved, apiInterface.loginUser(newUserData))
    }

    private fun loginSignupUser(
        btn: TextView,
        progress: ProgressBar,
        isLoginSaved: Boolean,
        apiFn: Call<LoginRegisterResponse>
    ) {
        btn.text = ""
        btn.isClickable = false
        progress.visibility = View.VISIBLE
        if (AppHelper.isConnected(context)) {

            apiFn
                .enqueue(
                    object : Callback<LoginRegisterResponse?> {

                        override fun onResponse(
                            call: Call<LoginRegisterResponse?>,
                            response: Response<LoginRegisterResponse?>
                        ) {
//                            btn.text =context.getString(R.string.signup_btn)
                            btn.isClickable = true
                            progress.visibility = View.GONE

                            if (response.isSuccessful) {
                                 token = response.body()?.token.toString()
                                if (isLoginSaved) {
                                    LocalSharedPreferences.prefPutString(context,"token",token!!)
                                }

                                Toast.makeText(
                                    context,
                                    "Successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                var intent = Intent(context, HomeActivity::class.java)
                                context.startActivity(intent)
                                (context as AuthActivity).finish()

                            } else {

                                Toast.makeText(context, response.errorBody()?.string(), Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onFailure(call: Call<LoginRegisterResponse?>, t: Throwable) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                )
        }
    }

    fun getUserDetail(){
        (context as ProfileActivity).profileDataView.visibility=View.GONE
        context.progressBar.visibility=View.VISIBLE
       val token=LocalSharedPreferences.getToken(context,"token")
        if(AppHelper.isConnected(context)){
            apiInterface.getUserDetail(token!!).enqueue(object :Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful){
                        context.progressBar.visibility=View.GONE
                        context.attachData(response.body()!!)
                        context.profileDataView.visibility=View.VISIBLE
                    }else {
                        Toast.makeText(context, "User Fetch Error", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

    }
}