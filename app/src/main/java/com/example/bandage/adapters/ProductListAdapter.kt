import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bandage.*
import com.example.bandage.activities.HomeActivity
import com.example.bandage.activities.ProductDetailActivity
import com.example.bandage.models.ProductModel
import com.example.bandage.models.request_model.AddToCartRequest
import com.example.bandage.requests.CartRequests

class ProductListAdapter(
    private var context: Context,
    private var productList: ArrayList<ProductModel>,
//    private var activityResultLauncher: ActivityResultLauncher<Intent>,
//    private var productStatusList: ArrayList<Boolean>
) :
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImageView: ImageView = itemView.findViewById<ImageView>(R.id.grid_card_cover_img)
        var productNameView: TextView = itemView.findViewById<TextView>(R.id.grid_card_title)
        var productBrandView: TextView = itemView.findViewById<TextView>(R.id.grid_card_brand)
        var productPriceView: TextView = itemView.findViewById<TextView>(R.id.grid_card_price_1)
        var productDiscountedPriceView: TextView =
            itemView.findViewById<TextView>(R.id.grid_card_discount_price)
        var productGridTile: View = itemView.findViewById<View>(R.id.grid_card)
        var addToCartBtn: TextView = itemView.findViewById<TextView>(R.id.grid_add_to_cart)
        var progressBar: ProgressBar =
            itemView.findViewById<ProgressBar>(R.id.grid_add_to_cart_progress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

//        if (productStatusList.size == position) {
//            productStatusList.add(false)
//        }
        val productModel = productList.get(position)
        val cartRequest = CartRequests(context)
//        holder.productImageView.setImageResource(productModel.images)
        holder.productNameView.text = productModel.name
        holder.productBrandView.text = productModel.category
        holder.productPriceView.text = "$ ${productModel.price}"
        holder.productDiscountedPriceView.text = "$ ${productModel.price}"
        Glide.with(context).load(productList[position].image).fitCenter()
            .into(holder.productImageView)

        if (productModel.productId != null) {
            holder.addToCartBtn.text = "Go to Cart"
            holder.addToCartBtn.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
            (context as HomeActivity).setOnClickToCart(holder.addToCartBtn)


        } else {
            holder.addToCartBtn.text = "Add"
            holder.addToCartBtn.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.primary))

            holder.addToCartBtn.setOnClickListener {
                cartRequest.addToCart(
                    AddToCartRequest(productModel.price, productModel.id),
                    holder.addToCartBtn,
                    holder.progressBar
                )
            }
        }

        holder.productGridTile.setOnClickListener {

            var detailIntent = Intent(context, ProductDetailActivity::class.java)
            detailIntent.putExtra("productId", productModel.id)
            detailIntent.putExtra("isProductAdded", productModel.productId==productModel.id)
            (context as HomeActivity).startActivityForResult(detailIntent,ProductDetailActivity().resultCode)

        }
//        if (productStatusList[position]) {
//            updateToGoToCartButton(context, holder.addToCartBtn, activityResultLauncher, position)
//        } else {
//            holder.addToCartBtn.text = "Add"
//            holder.addToCartBtn.backgroundTintList =
//                ColorStateList.valueOf(context.getColor(R.color.primary))
//            holder.addToCartBtn.setOnClickListener {
//                productStatusList[position] = true;
//                cartList.add(CartProduct(productModel.id, 1))
//                updateToGoToCartButton(
//                    context,
//                    holder.addToCartBtn,
//                    activityResultLauncher,
//                    position
//                )
//
//            }
//        }


    }

}