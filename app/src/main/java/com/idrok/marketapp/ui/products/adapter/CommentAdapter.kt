package com.idrok.marketapp.ui.products.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Comments
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.rv_item_comments.view.*
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(options: FirestoreRecyclerOptions<Comments>) :FirestoreRecyclerAdapter<Comments,CommentAdapter.VH>(options) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_comments,parent,false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int, model: Comments) {
        holder.onBind(model)
    }



    class VH(itemView:View):RecyclerView.ViewHolder(itemView){

        fun onBind(model: Comments) {
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            itemView.tv_comment_user_name.text = model.user_name
            itemView.tv_comment_date.text = simpleDateFormat.format(model.date?.toDate()!!)
            itemView.tv_comment.text = model.comment
            itemView.cv_comment_avatar.background.setTint(model.user_color)
        }
    }

}