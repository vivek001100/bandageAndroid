package com.example.bandage.models.response_model

import com.example.bandage.models.ProductModel

data class ProductsResponse(
    val products: List<ProductModel>
)