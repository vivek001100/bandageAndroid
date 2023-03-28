package com.example.bandage.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bandage.activities.HomeActivity
import com.example.bandage.R


class EmptyCartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_empty_cart, container, false)
        var addNowBtn = view.findViewById<TextView>(R.id.empty_cart_add_now_btn)
        addNowBtn.setOnClickListener {
            var newIntent = Intent(context, HomeActivity::class.java)
            activity?.onBackPressed()
        }
        return view
    }
}