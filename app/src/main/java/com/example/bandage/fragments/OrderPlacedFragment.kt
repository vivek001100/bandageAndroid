package com.example.bandage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bandage.R


class OrderPlacedFragment(private val orderId: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_order_placed, container, false)
        v.findViewById<TextView>(R.id.order_id).text = orderId
        v.findViewById<TextView>(R.id.back_to_home).setOnClickListener {
            activity?.onBackPressed()
        }
        return v;
    }
}