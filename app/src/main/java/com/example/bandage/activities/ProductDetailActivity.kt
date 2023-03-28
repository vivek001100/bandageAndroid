package com.example.bandage.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bandage.R
import com.example.bandage.models.getCartCount
import com.example.bandage.models.request_model.AddToCartRequest
import com.example.bandage.models.response_model.Product
import com.example.bandage.requests.CartRequests
import com.example.bandage.requests.ProductRequests

class ProductDetailActivity : AppCompatActivity() {
    val resultCode = 2
    private var deletedList: ArrayList<String>? = null
    lateinit var progressBar: ProgressBar
    lateinit var addToCartProgressBar: ProgressBar
    lateinit var scrollView: ScrollView
    private lateinit var detailImageView1: ImageView
    private lateinit var detailImageView2: ImageView
    private lateinit var detailProductNameView: TextView
    private lateinit var detailProductPriceView: TextView
    private lateinit var detailProductDescriptionView: TextView
    private lateinit var detailCartCount: TextView
    lateinit var addToCartBtn: TextView

    private lateinit var productId: String
    private var status: String = "not_added"

    lateinit var price: String


    fun updateCartCount() {
        var count = getCartCount()
        if (count == 0) {
            detailCartCount.visibility = View.GONE
        } else {
            detailCartCount.visibility = View.VISIBLE

            detailCartCount.text = "$count"
        }
    }

    private fun init() {
        addToCartBtn = findViewById<TextView>(R.id.detail_add_to_cart)
        scrollView = findViewById<ScrollView>(R.id.detail_scrollView)
        progressBar = findViewById<ProgressBar>(R.id.detail_progress_bar)
        detailImageView1 = findViewById<ImageView>(R.id.detail_cover_image_1)
        detailImageView2 = findViewById<ImageView>(R.id.detail_cover_image_2)
        detailProductNameView = findViewById<TextView>(R.id.detail_product_name)
        detailProductPriceView = findViewById<TextView>(R.id.detail_product_price)
        detailProductDescriptionView = findViewById(R.id.detail_product_description)
        addToCartProgressBar = findViewById(R.id.detail_add_to_cart_progress_bar)
        addToCartBtn = findViewById<TextView>(R.id.detail_add_to_cart)
        detailCartCount = findViewById(R.id.detail_cart_count)
    }

    fun attachData(productDetail: Product) {
        Glide.with(this).load(productDetail.images[0]).fitCenter()
            .into(detailImageView1)
        Glide.with(this).load(productDetail.images[0]).fitCenter()
            .into(detailImageView2)
        detailProductNameView.text = productDetail.name
        detailProductPriceView.text = productDetail.price
        detailProductDescriptionView.text = productDetail.meta.description
        price = productDetail.price

        findViewById<ImageView>(R.id.detail_cart_btn).setOnClickListener {
            startActivityForResult(
                Intent(this, CartActivity::class.java),
                CartActivity().resultCode
            )
        }
        val isProductAdded = intent.getBooleanExtra("isProductAdded", false)
        if (isProductAdded) {
            setToGotoCart()
        } else {
            setToAddtoCart()
        }
    }

    fun setToGotoCart() {
        addToCartBtn.text = "Go to Cart"
        addToCartBtn.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
        addToCartBtn.setOnClickListener {
            startActivityForResult(
                Intent(this, CartActivity::class.java),
                CartActivity().resultCode
            )
        }
    }

    private fun setToAddtoCart() {
        val cartRequests = CartRequests(this)
        addToCartBtn.text = "Add"
        addToCartBtn.backgroundTintList = ColorStateList.valueOf(getColor(R.color.primary))
        addToCartBtn.setOnClickListener {
            cartRequests.addToCart(
                AddToCartRequest(price, productId),
                addToCartBtn,
                addToCartProgressBar, false
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        init()

        val thisIntent = intent
        productId = thisIntent.getStringExtra("productId")!!
        val productRequests = ProductRequests(this)
        productRequests.getProductById(productId)


        var backButton = findViewById<ImageView>(R.id.detail_back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {

        Log.i("logg", "handleOnBackPressed: ")
        var thisIntent = intent
        thisIntent.putExtra("id", productId)
        thisIntent.putExtra("status", status)
        if (deletedList != null) {
            thisIntent.putExtra("deletedList", deletedList)
        }
        setResult(resultCode, intent)
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateCartCount()
        if (requestCode == CartActivity().resultCode) {
            deletedList = data?.getStringArrayListExtra("deletedList") as ArrayList<String>
            Log.i("deletedList", "onActivityResult: $deletedList")
            if (deletedList != null) {
                if (deletedList!!.contains(productId)) {
                    Log.i("deletedList", "onActivityResult:included!! ")
                    setToAddtoCart()
                }
            }
        }
    }

}