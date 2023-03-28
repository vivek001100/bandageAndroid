package com.example.bandage.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bandage.R
import com.example.bandage.models.getCartCount
import com.example.bandage.models.productList
import com.example.bandage.requests.ProductRequests


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerGridView: RecyclerView
    private lateinit var cartCount: TextView

    fun updateCartCount() {
        var count = getCartCount()
        if (count == 0) {
            cartCount.visibility = View.GONE
        } else {
            cartCount.visibility = View.VISIBLE
            cartCount.text = "$count"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerGridView = findViewById<RecyclerView>(R.id.recycler_grid_view)

        var progressBar = findViewById<ProgressBar>(R.id.products_progress_bar)
        var cartBtn = findViewById<ImageView>(R.id.home_page_cart_btn)
        cartCount = findViewById(R.id.home_cart_count)
        setOnClickToCart(cartBtn)

        val request = ProductRequests(this)
        recyclerGridView.layoutManager = GridLayoutManager(this, 2)
        request.getProducts(progressBar, recyclerGridView)
        findViewById<ImageView>(R.id.home_page_profile_btn).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }

    fun setOnClickToCart(btn: View) {

        btn.setOnClickListener {
            startActivityForResult(
                Intent(this, CartActivity::class.java),
                CartActivity().resultCode
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateCartCount()


        if (requestCode == CartActivity().resultCode) {

            val deletedCartList = data?.getStringArrayListExtra("deletedList")
            Log.i("deletedProductList", "onActivityResult:$deletedCartList ")

            deletedCartList?.forEach { e ->
                var position: Int = productList.indexOfFirst { p ->
                    p.id == e
                }
                productList[position].productId = null
                recyclerGridView.adapter?.notifyItemChanged(position)

            }
        } else if (requestCode == ProductDetailActivity().resultCode) {

            val id = data?.getStringExtra("id")
            val deletedList = data?.getStringArrayListExtra("deletedList") as ArrayList<String>?
            if (deletedList != null) {
                deletedList.forEach { id ->
                    var position: Int = productList.indexOfFirst { p ->
                        p.id == id
                    }
                    recyclerGridView.adapter?.notifyItemChanged(position)
                }
            } else {
                var position: Int = productList.indexOfFirst { p ->
                    p.id == id
                }
                recyclerGridView.adapter?.notifyItemChanged(position)
            }


        }
    }
}

