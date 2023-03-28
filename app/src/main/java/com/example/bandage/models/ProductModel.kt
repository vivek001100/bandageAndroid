package com.example.bandage.models

data class ProductModel(
    val category: String,
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    var productId:String?
)

lateinit var productList: ArrayList<ProductModel>

fun getCartCount():Int{
    return productList.count {productModel ->
        productModel.productId!=null
    }
}