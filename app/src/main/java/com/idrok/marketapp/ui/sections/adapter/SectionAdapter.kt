package com.idrok.marketapp.ui.sections.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.model.callFirebase.CallStorage
import kotlinx.android.synthetic.main.rv_section_page_item.view.*

class SectionAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val listImg :ArrayList<SubCollections>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<SectionAdapter.VH>() {
    private val storage = CallStorage()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(context).inflate(R.layout.rv_section_page_item, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(position)
        }
        Log.d("sectionAdapter", "adapter:$list")
        holder.onBind(list[position],listImg[position],context,storage)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(
            title: String,
            subCollections: SubCollections,
            context: Context,
            storage: CallStorage
        ) {
            itemView.tv_section_page.text = title
            storage.downloadImages(subCollections.img_path,itemView.iv_section_page,context)
        }
    }
}