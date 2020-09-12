package com.idrok.marketapp.ui.news.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.News
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.rv_news_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewsRvAdapter(
    options: FirestoreRecyclerOptions<News>,
    private val hashMap: HashMap<String, Boolean>,
    private val callback: ((News) -> Unit)
) : FirestoreRecyclerAdapter<News, NewsRvAdapter.VH>(options) {
    private val storage = CallStorage()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_news_item, parent, false)
        return VH(view, parent.context)
    }

    override fun onBindViewHolder(holder: VH, position: Int, model: News) {
        holder.itemView.setOnClickListener {
            callback.invoke(model)
        }
        holder.onBind(model, hashMap, storage)
    }

    class VH(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        fun onBind(news: News, hashMap: HashMap<String, Boolean>, storage: CallStorage) {
            if (!hashMap.containsKey(news.doc_path)) {
                hashMap[news.doc_path] = false
            }
            itemView.tv_news_title.text = news.title
            itemView.tv_news_content.text = news.content
            if (news.img_path.isNotEmpty()) {
                storage.downloadImages(news.img_path, itemView.iv_news, context)
            }
            itemView.tv_news_date.text =
                simpleDateFormat.format(news.last_update!!.toDate()).toString()
            Log.d("NewsFragment","hashMap:${hashMap[news.doc_path]}")
            if (hashMap[news.doc_path]!!) {
                itemView.tv_news_title.setTextColor(context.getColor(R.color.lightGrey))
                itemView.tv_news_content.setTextColor(context.getColor(R.color.lightGrey))
            } else {
                itemView.tv_news_title.setTextColor(context.getColor(R.color.black))
                itemView.tv_news_content.setTextColor(context.getColor(R.color.black))
            }
        }
    }

}