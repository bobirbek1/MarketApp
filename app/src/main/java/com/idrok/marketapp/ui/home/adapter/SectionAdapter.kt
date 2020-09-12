package com.idrok.marketapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.model.callFirebase.CallStorage
import kotlinx.android.synthetic.main.rv_section_item.view.*

class SectionAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val listSubCollections: ArrayList<SubCollections>,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<SectionAdapter.VH>() {
    private var rowIndex = 0
    private val storage = CallStorage()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_section_item, parent, false)
        return VH(view)
    }


    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(position, list, listSubCollections[position],context,storage)
        holder.itemView.setOnClickListener {
            rowIndex = position
            itemClickListener.invoke(position)
            notifyDataSetChanged()
        }

        if (rowIndex == position) {
            holder.itemView.cl_section_bg_1.background =
                ContextCompat.getDrawable(context,R.drawable.section_bg_grey)
        } else {
            holder.itemView.cl_section_bg_1.background =
                ContextCompat.getDrawable(context,R.drawable.section_bg)
        }
    }

    class VH(v: View,) : RecyclerView.ViewHolder(v) {

        fun onBind(
            position: Int,
            list: ArrayList<String>,
            subCollections: SubCollections,
            context: Context,
            storage: CallStorage
        ) {
            itemView.tv_section.text = list[position]
            storage.downloadImages(subCollections.img_path,itemView.iv_section,context)
        }
    }
}