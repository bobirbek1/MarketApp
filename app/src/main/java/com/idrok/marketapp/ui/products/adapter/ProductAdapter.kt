package com.idrok.marketapp.ui.products.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import kotlinx.android.synthetic.main.rv_product_item.view.*

class ProductAdapter(
    private val itemClickListener: (Product) -> Unit,
    options: FirestorePagingOptions<Product>
) : FirestorePagingAdapter<Product, ProductAdapter.VH>(options) {

    private val callStorage = CallStorage()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_product_item, parent, false)
        return VH(view,parent.context)
    }

    override fun onBindViewHolder(holder: VH, position: Int, model: Product) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(model)
        }
        holder.onBind(model,callStorage)
    }

    class VH(v: View, val context: Context) : RecyclerView.ViewHolder(v) {
        fun onBind(
            model: Product,
            callStorage: CallStorage
        ) {
            itemView.tv_product_name.text = model.name
            itemView.tv_product_description.text = model.company_name
            itemView.tv_product_cost.text = model.cost
            if (!model.image_path.isNullOrEmpty() && model.image_path[0].isNotEmpty()){
                Log.d("image_path","path:${model.image_path[0]}")
                callStorage.downloadImages(model.image_path[0],itemView.iv_product,context)
            }
        }
    }
}
