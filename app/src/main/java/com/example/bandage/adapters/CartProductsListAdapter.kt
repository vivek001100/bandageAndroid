package com.example.bandage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandage.*
import com.example.bandage.activities.CartActivity
import com.example.bandage.models.request_model.UpdateQuantityRequest
import com.example.bandage.models.response_model.CartProduct
import com.example.bandage.requests.CartRequests

class CartProductsListAdapter(
    var cartProductsList: ArrayList<CartProduct>,
) :
    RecyclerView.Adapter<CartProductsListAdapter.CartViewHolder>() {
    lateinit var context: Context
    lateinit var cartRequests: CartRequests

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartProductImage: ImageView =
            itemView.findViewById<ImageView>(R.id.cart_product_tile_image)
        var cartProductName: TextView = itemView.findViewById<TextView>(R.id.cart_product_tile_name)
        var cartProductPrice: TextView =
            itemView.findViewById<TextView>(R.id.cart_product_tile_price)
        var cartProductBrand: TextView =
            itemView.findViewById<TextView>(R.id.cart_product_tile_brand)
        var spinnerView: Spinner = itemView.findViewById<Spinner>(R.id.cart_product_tile_spinner)
        var deleteBtn: TextView = itemView.findViewById<TextView>(R.id.cart_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        context = parent.context
        cartRequests = CartRequests(context)

        val view = LayoutInflater.from(context).inflate(R.layout.cart_product_tile, parent, false)

        return CartViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return cartProductsList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        try {

            val cartModel = cartProductsList.get(position)
//            val productModel = productList.filter { product -> (product.id == cartModel.productId) }[0]
//            holder.cartProductImage.setImageResource(cartModel.images[0])
            Glide.with(context).load(cartModel.images[0]).fitCenter()
                .into(holder.cartProductImage)
            holder.cartProductBrand.text = cartModel.category
            holder.cartProductPrice.text = "$ ${cartModel.price}"
            holder.cartProductName.text = cartModel.name

            holder.deleteBtn.setOnClickListener {

                cartRequests.deleteCartProduct(
                    UpdateQuantityRequest(
                        cartModel.productId,
                        1,
                    ),
                    { notifyItemRemoved(position) },
                    position,
                )
            }


            ArrayAdapter.createFromResource(
                context,
                R.array.planets_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                holder.spinnerView.adapter = adapter
                holder.spinnerView.setSelection(adapter.getPosition(cartModel.quantity.toString()))
            }

            holder.spinnerView.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        holder.spinnerView.setSelection(p2)
                        val quantity = Integer.parseInt(p0?.getItemAtPosition(p2).toString())
                        if (quantity != cartModel.quantity) {
                            cartRequests.updateQuantity(
                                UpdateQuantityRequest(
                                    cartModel.productId,
                                    quantity,
                                    ),
                                { notifyItemChanged(position) },
                                { (context as CartActivity).updateTotalPrice() },
                                position
                            )
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
