package com.example.bandage.requests

import ProductListAdapter
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bandage.activities.HomeActivity
import com.example.bandage.activities.ProductDetailActivity
import com.example.bandage.api.ApiInterface
import com.example.bandage.api.RetrofitClient
import com.example.bandage.helper.LocalSharedPreferences
import com.example.bandage.models.ProductModel
import com.example.bandage.models.productList
import com.example.bandage.models.response_model.ProductByIdResponse
import com.example.bandage.models.response_model.ProductsResponse
import com.example.myapplication.helper.AppHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductRequests(val context: Context) {

    private var token = LocalSharedPreferences.getToken(context, "token")

    private val apiInterface: ApiInterface =
        RetrofitClient.getInstance().create(ApiInterface::class.java)

    fun getProducts(progressBar: ProgressBar, recyclerView: RecyclerView) {

        progressBar.visibility = View.VISIBLE
        if (AppHelper.isConnected(context)) {
            apiInterface.getProducts(token!!).enqueue(object : Callback<ProductsResponse> {
                override fun onResponse(
                    call: Call<ProductsResponse>,
                    response: Response<ProductsResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        productList = response.body()?.products as ArrayList<ProductModel>
                        (context as HomeActivity).updateCartCount()
                        recyclerView.adapter = ProductListAdapter(
                            context,
                            productList
                        )
                    } else {
                        Toast.makeText(context, "Products fetching error", Toast.LENGTH_LONG)
                            .show()
                    }

                }

                override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }

            })
        }
    }

    fun getProductById(productId: String) {

        if (AppHelper.isConnected(context)) {
            (context as ProductDetailActivity).progressBar.visibility = View.VISIBLE
            context.scrollView.visibility = View.GONE
            apiInterface.getProductDetailsById(token!!,productId)
                .enqueue(object : Callback<ProductByIdResponse> {
                    override fun onResponse(
                        call: Call<ProductByIdResponse>,
                        response: Response<ProductByIdResponse>
                    ) {
                        if (response.isSuccessful) {
                            context.progressBar.visibility = View.GONE
                            context.attachData(response.body()?.product!!)
                            context.scrollView.visibility = View.VISIBLE
                            context.updateCartCount()
                        } else {
                            Toast.makeText(context, "Product fetching error", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<ProductByIdResponse>, t: Throwable) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }

}