package com.example.bandage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.bandage.fragments.NonEmptyCartFragment
import com.example.bandage.R
import com.example.bandage.fragments.EmptyCartFragment
import com.example.bandage.fragments.OrderPlacedFragment
import com.example.bandage.models.response_model.CartProduct
import com.example.bandage.models.response_model.cartProductsList
import com.example.bandage.requests.CartRequests


class CartActivity : AppCompatActivity() {
    var deletedProductList = arrayListOf<String>()
    val resultCode = 1
    private lateinit var orderValueTextView: TextView
    private lateinit var totalValueTextView: TextView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        deletedProductList.clear()
        progressBar = findViewById<ProgressBar>(R.id.cart_progress_bar)
        val cartRequests = CartRequests(this)

        cartRequests.getCartProduct()


        findViewById<ImageView>(R.id.cart_back_button).setOnClickListener {
            onBackPressed()
        }

    }

    fun initData(
        orderValueView: TextView,
        totalValueView: TextView,
    ) {
        orderValueTextView = orderValueView
        totalValueTextView = totalValueView
    }

    fun updateTotalPrice() {

        var totalAmount = 0
        cartProductsList.forEach { e ->
            totalAmount += e.amount.toInt() * e.quantity
        }

        orderValueTextView.text = "$ $totalAmount"
        totalValueTextView.text = "$ $totalAmount"

    }


    override fun onBackPressed() {
        Log.i("deletedProductList", "onBackPressed:${deletedProductList} ")
        var thisIntent = intent
        thisIntent.putStringArrayListExtra("deletedList", deletedProductList)
        setResult(resultCode, thisIntent)

        super.onBackPressed()
    }


    fun attachFragment(orderId:String?) {
        if(orderId==null){
            if (cartProductsList.isNotEmpty())
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cart_frame_layout, NonEmptyCartFragment())
                    .commit()
            else
                supportFragmentManager.beginTransaction()
                    .replace(R.id.cart_frame_layout, EmptyCartFragment())
                    .commit()
        }
        else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.cart_frame_layout, OrderPlacedFragment(orderId))
                .commit()
        }
    }
}



