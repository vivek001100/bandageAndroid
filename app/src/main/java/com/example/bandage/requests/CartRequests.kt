package com.example.bandage.requests

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bandage.activities.CartActivity
import com.example.bandage.activities.HomeActivity
import com.example.bandage.activities.ProductDetailActivity
import com.example.bandage.api.ApiInterface
import com.example.bandage.api.RetrofitClient
import com.example.bandage.fragments.EmptyCartFragment
import com.example.bandage.fragments.NonEmptyCartFragment
import com.example.bandage.helper.LocalSharedPreferences
import com.example.bandage.models.ProductModel
import com.example.bandage.models.productList
import com.example.bandage.models.request_model.AddToCartRequest
import com.example.bandage.models.request_model.UpdateQuantityRequest
import com.example.bandage.models.response_model.*
import com.example.myapplication.helper.AppHelper
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRequests(private val context: Context) {
    private var token = LocalSharedPreferences.getToken(context, "token")

    private val apiInterface: ApiInterface =
        RetrofitClient.getInstance().create(ApiInterface::class.java)

    fun addToCart(
        addToCartBody: AddToCartRequest,
        btn: TextView,
        progressBar: ProgressBar,
        isFromHome: Boolean = true
    ) {
        btn.text = ""
        btn.isClickable = false

        progressBar.visibility = View.VISIBLE
        apiInterface.addToCart(token!!, addToCartBody).enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    productList.forEach { e ->
                        if (e.id == addToCartBody.productId) {
                            e.productId = addToCartBody.productId
                        }
                    }
                    btn.isClickable = true
                    btn.text = "Go to Cart"
                    btn.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)

                    if (isFromHome) {
                        (context as HomeActivity).setOnClickToCart(btn)
                        context.updateCartCount()

                    } else {
                        (context as ProductDetailActivity).setToGotoCart()
                        context.updateCartCount()
                    }



                } else {
                    btn.text = "Add"
                    Toast.makeText(context, "Products fetching error", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun getCartProduct() {
        (context as CartActivity).progressBar.visibility = View.VISIBLE

        apiInterface.getCartProducts(token!!).enqueue(
            object : Callback<CartProductsResponse> {
                override fun onResponse(
                    call: Call<CartProductsResponse>,
                    response: Response<CartProductsResponse>
                ) {
                    if (response.isSuccessful) {
                        context.progressBar.visibility = View.GONE
                        cartProductsList =
                            response.body()?.cartProducts as ArrayList<CartProduct>

                        (context as CartActivity).attachFragment(null)
                    } else {
                        Toast.makeText(context, "Cart-Products fetching error", Toast.LENGTH_LONG)
                            .show()
                    }

                }

                override fun onFailure(call: Call<CartProductsResponse>, t: Throwable) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            },
        )
    }

//    fun getTotalAmount() {
//        apiInterface.getTotalAmount(token!!).enqueue(object : Callback<TotalAmountResponse> {
//            override fun onResponse(
//                call: Call<TotalAmountResponse>,
//                response: Response<TotalAmountResponse>
//            ) {
//                if (response.isSuccessful) {
//
//                }
//            }
//
//            override fun onFailure(call: Call<TotalAmountResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    fun updateQuantity(
        body: UpdateQuantityRequest,
        adapterNotifyItemChanged: () -> Unit,
        updateTotalPrice: () -> Unit,
        index: Int
    ) {
        apiInterface.updateQuantity(token!!, body).enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                if (response.isSuccessful) {
                    cartProductsList[index].quantity = body.quantity
                    adapterNotifyItemChanged()
                    updateTotalPrice()
                } else {
                    Toast.makeText(context, "Quantity Updating error", Toast.LENGTH_LONG)
                        .show()
                }

            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun deleteCartProduct(
        body: UpdateQuantityRequest,
        adapterNotifyItemDelete: () -> Unit,
        index: Int
    ) {

        if (AppHelper.isConnected(context)) {

            apiInterface.deleteCartProduct(token!!, body)
                .enqueue(object : Callback<SuccessResponse> {
                    override fun onResponse(
                        call: Call<SuccessResponse>,
                        response: Response<SuccessResponse>
                    ) {

                        if (response.isSuccessful) {

                            (context as CartActivity).deletedProductList.add(body.productId)
                            productList.forEach { e ->
                                if (e.id == body.productId) {
                                    e.productId = null
                                }
                            }
                            cartProductsList.removeAt(index)
                            adapterNotifyItemDelete()
                            context.attachFragment(null)
                            context.updateTotalPrice()
                        } else {
                            Toast.makeText(context, "Cart deleting error", Toast.LENGTH_LONG)
                                .show()
                        }

                    }

                    override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }

    }

    fun placeOrder() {
        if (AppHelper.isConnected(context)) {
            apiInterface.placeOrder(token!!).enqueue(object : Callback<PlaceOrderResponse> {
                override fun onResponse(
                    call: Call<PlaceOrderResponse>,
                    response: Response<PlaceOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        cartProductsList.forEach { e ->
                            (context as CartActivity).deletedProductList.add(e.productId)
                            productList.forEach { p ->
                                if (p.id == e.productId) {
                                    p.productId = null
                                }
                            }
                        }
                        cartProductsList.clear()

                        Log.i(
                            "back_to_home",
                            "onBackPressed:${(context as CartActivity).deletedProductList} "
                        )

                        (context as CartActivity).attachFragment(response.body()?.orderId)
                    } else {
                        Toast.makeText(context, "Order Placing Error", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<PlaceOrderResponse>, t: Throwable) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }

            })
        }
    }
}