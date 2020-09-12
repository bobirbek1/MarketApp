package com.idrok.marketapp.ui.products.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import kotlinx.android.synthetic.main.rv_product_page_item.view.*

class ProductsPageAdapter(
    private val itemClickListener: ((Product) -> Unit),
    options: FirestorePagingOptions<Product>
) : FirestorePagingAdapter<Product, ProductsPageAdapter.VH>(options) {

    private val storage = CallStorage()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_product_page_item, parent, false)
        return VH(view, parent.context)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        model: Product
    ) {
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(model)
        }
        holder.onBind(model,storage)
    }

    class VH(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        fun onBind(model: Product, storage: CallStorage) {
            itemView.tv_product_page_cost.text = model.cost
            itemView.tv_product_page_name.text = model.name
            itemView.tv_product_page_description.text = model.description
            itemView.tv_product_views.text = model.views.toString()
            storage.downloadImages(model.image_path[0],itemView.iv_product_page,context)
        }
    }
}



