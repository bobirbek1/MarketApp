package com.idrok.marketapp.ui.products.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import kotlinx.android.synthetic.main.rv_title_page_item.view.*

class TitlePageAdapter(
    private val list: ArrayList<String>,
    private val onItemClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<TitlePageAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_title_page_item, parent, false)
        return VH(
            view
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(list[position])
        }
        holder.onBind(list[position])
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun onBind(
            title: String
        ) {
            itemView.tv_page_title.text = title
        }
    }
}