package com.example.bandage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bandage.R
import com.example.bandage.activities.CartActivity
import com.example.bandage.adapters.CartProductsListAdapter
import com.example.bandage.models.response_model.cartProductsList
import com.example.bandage.requests.CartRequests


class NonEmptyCartFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_non_empty_cart, container, false)
        (context as CartActivity).initData(
            v.findViewById<TextView>(R.id.cart_order_value),
            v.findViewById<TextView>(R.id.cart_total_amount)
        )


        var cartRecyclerView = v.findViewById<RecyclerView>(R.id.cart_recycler_view)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)

        cartRecyclerView.adapter = CartProductsListAdapter(cartProductsList)
        (context as CartActivity).updateTotalPrice()
        val cartRequests = CartRequests(requireContext())


        v.findViewById<TextView>(R.id.cart_place_order_btn).setOnClickListener {
            cartRequests.placeOrder()
        }
        return v
    }


}
