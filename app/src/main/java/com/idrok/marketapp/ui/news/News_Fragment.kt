package com.idrok.marketapp.ui.news

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.News
import com.idrok.marketapp.splashUi.NEWS_DOC_PATH
import com.idrok.marketapp.ui.news.adapter.NewsRvAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news.view.*

class NewsFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var newsBell: MenuItem
    private lateinit var prefs:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_news, container, false)
        setHasOptionsMenu(true)
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        newsBell = menu.findItem(R.id.news_bell)
        newsBell.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        setViews()
        setRv()
    }

    private fun setRv() {
        val query = FirebaseFirestore.getInstance().collection("yangiliklar")
            .orderBy("last_update", Query.Direction.DESCENDING)
        val option = getOption(query)
        val hashMap = getHashMap()
        val adapter = NewsRvAdapter(option,hashMap){news ->
            val bundle = Bundle()
            bundle.putString("news",Gson().toJson(news))
            findNavController().navigate(R.id.newsPage,bundle)
        }
        rootView.rv_news.adapter = adapter
    }

    private fun getHashMap(): HashMap<String,Boolean> {
        val string = prefs.getString(NEWS_DOC_PATH,"")
        val type = object : TypeToken<HashMap<String,Boolean>>() {}.type
        val hashMap = Gson().fromJson<HashMap<String,Boolean>>(string,type)
        Log.d("NewsFragment","getHashMap:$hashMap")
        return hashMap
    }

    private fun getOption(query: Query): FirestoreRecyclerOptions<News> =
        FirestoreRecyclerOptions.Builder<News>()
            .setLifecycleOwner(this)
            .setQuery(query,News::class.java)
            .build()

    private fun setViews() {
//        requireActivity().toolbar.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
        requireActivity().cl.visibility = View.GONE
        requireActivity().fab.hide()
        requireActivity().nav_view.visibility = View.GONE
    }

}