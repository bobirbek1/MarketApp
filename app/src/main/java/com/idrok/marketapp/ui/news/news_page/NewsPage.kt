package com.idrok.marketapp.ui.news.news_page

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.News
import com.idrok.marketapp.model.callFirebase.CallFirestore
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.idrok.marketapp.splashUi.NEWS_DOC_PATH
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news_page.view.*
import java.text.SimpleDateFormat
import java.util.*

class NewsPage : Fragment() {

    private lateinit var rootView: View
    private lateinit var gson: Gson
    private lateinit var prefs: SharedPreferences
    private lateinit var firestore: CallFirestore
    private lateinit var storage: CallStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_news_page, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        gson = Gson()
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        firestore = CallFirestore(requireContext())
        storage = CallStorage()
        getNewsFromArg()
        getDocPath()
        setViews()
    }

    private fun getDocPath(): String = requireArguments().getString("DOC_PATH", "")

    private fun removeNewsFromUnviewed(news: News) {
        val string = prefs.getString(NEWS_DOC_PATH, "")
        val type = object : TypeToken<HashMap<String, Boolean>>() {}.type
        var hashMap = hashMapOf<String, Boolean>()
        Log.d("NewsPage","removeNews:$hashMap, string:$string")
        if (!string.isNullOrEmpty()) {
            hashMap = gson.fromJson<HashMap<String, Boolean>>(string, type)
            hashMap[news.doc_path] = true
            Log.d("NewsPage","changeHashMap:$hashMap")
        } else {
            hashMap[news.doc_path] = true
            Log.d("NewsPage","changeHashMap:$hashMap")
        }
        saveHashMap(hashMap)
    }

    private fun saveHashMap(hashMap: java.util.HashMap<String, Boolean>) {
        val string = gson.toJson(hashMap)
        prefs.edit()
            .putString(NEWS_DOC_PATH, string)
            .apply()
    }

    private fun getNewsFromArg() {
        val string = requireArguments().getString("news")
        if (string.isNullOrEmpty() && string.isNullOrBlank()) {
            val docPath = getDocPath()
            firestore.getNews(docPath) { news ->
                Log.d("NewsPage","getDocFromServer:$news")
                removeNewsFromUnviewed(news)
                setVariables(news)
            }
        } else {
            val news = gson.fromJson(string, News::class.java)
            Log.d("NewsPage","getDocFrombundle:$news")
            removeNewsFromUnviewed(news)
            setVariables(news)
        }
    }

    private fun setVariables(news: News) {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        rootView.tv_news_page_title.text = news.title
        rootView.tv_news_page_content.text = news.content
        rootView.tv_news_page_date.text =
            simpleDateFormat.format(news.last_update!!.toDate()).toString()
        if (news.img_path.isNotEmpty()) {
            rootView.iv_news_image.visibility = View.VISIBLE
            storage.downloadImages(news.img_path, rootView.iv_news_image, requireContext())
        } else {
            rootView.iv_news_image.visibility = View.GONE
        }
        if (news.body.isNotEmpty()) {
            rootView.tv_news_page_body.visibility = View.VISIBLE
            rootView.tv_news_page_body.text = news.body
        } else {
            rootView.tv_news_page_body.visibility = View.GONE
        }
        if (news.product_path.isNotEmpty()){
            rootView.btn_news_page.visibility = View.VISIBLE
            rootView.btn_news_page.setOnClickListener {

            }
        } else {
            rootView.btn_news_page.visibility = View.GONE
        }
    }

    private fun setViews() {
        requireActivity().nav_view.visibility = View.GONE
        requireActivity().fab.hide()
        requireActivity().cl.visibility = View.GONE
    }

}