package com.example.bandage.api

import com.example.bandage.models.request_model.AddToCartRequest
import com.example.bandage.models.request_model.LoginRegisterRequest
import com.example.bandage.models.request_model.UpdateQuantityRequest
import com.example.bandage.models.response_model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @POST("/api/auth/register")
    fun registerUser(@Body registerUserRequest: LoginRegisterRequest): Call<LoginRegisterResponse>


    @POST("/api/auth/login")
    fun loginUser(@Body loginUserRequests: LoginRegisterRequest?): Call<LoginRegisterResponse>

    @POST("/api/cart/add")
    fun addToCart(
        @Header("token") auth_token: String,
        @Body addToCartBody: AddToCartRequest
    ): Call<SuccessResponse>

    @POST("/api/cart/update")
    fun updateQuantity(
        @Header("token") auth_token: String,
        @Body body: UpdateQuantityRequest
    ): Call<SuccessResponse>

    @GET("/api/product/all")
    fun getProducts(@Header("token") auth_token: String): Call<ProductsResponse>

    @GET("/api/product/{id}")
    fun getProductDetailsById(@Header("token") auth_token: String,@Path(value = "id") productId: String): Call<ProductByIdResponse>

    @GET("/api/cart/products")
    fun getCartProducts(@Header("token") auth_token: String): Call<CartProductsResponse>

    @GET("/api/cart/total")
    fun getTotalAmount(@Header("token") auth_token: String): Call<TotalAmountResponse>

    @POST("/api/cart/delete")
    fun deleteCartProduct(
        @Header("token") auth_token: String,
        @Body body: UpdateQuantityRequest
    ): Call<SuccessResponse>

    @GET ("/api/auth/getDetail")
    fun getUserDetail(@Header("token") auth_token: String):Call<UserResponse>

    @POST("/api/order/place")
    fun placeOrder(@Header("token") auth_token: String):Call<PlaceOrderResponse>
}
