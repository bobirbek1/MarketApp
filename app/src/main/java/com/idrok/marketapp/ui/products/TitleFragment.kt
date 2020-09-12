package com.idrok.marketapp.ui.products

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.model.callFirebase.MARKET
import com.idrok.marketapp.ui.home.LIST_SUBCOLLECTIONS
import com.idrok.marketapp.ui.products.adapter.TitlePageAdapter
import com.idrok.marketapp.ui.sections.POSITION
import com.idrok.marketapp.ui.sections.SECTION
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_page_titles.view.*

const val PATH = "path"
class TitleFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var section:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_page_titles, container, false)
        Log.d("TitlePageAdapter", arguments.toString())
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setViews()
       val bundle = arguments
        Log.d("TitlePageAdapter", requireArguments().toString())
        if (bundle != null)
        getTitles(bundle.getString(SECTION)!!,bundle.getInt(POSITION))
    }

    private fun setViews() {
        requireActivity().fab.show()
        requireActivity().cl.visibility = View.VISIBLE
    }

    private fun getTitles(section: String, position: Int) {
        val pref = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        val string = pref.getString(LIST_SUBCOLLECTIONS,"")
        val type = object : TypeToken<ArrayList<SubCollections>>() {}.type
        val list = Gson().fromJson<ArrayList<SubCollections>>(string,type)
        Log.d("TitlePageAdapter", "list:" + list[position].sub_collections.toString())
        setRv(section,list[position])
    }

    private fun setRv(
        section: String,
        subCollections: SubCollections
    ) {
        Log.d("TitlePageAdapter", subCollections.sub_collections.toString())
        val adapter = TitlePageAdapter(subCollections.sub_collections){title ->
            val bundle = getBundle(section,title)
            findNavController().navigate(R.id.pageProducts,bundle)
        }
        Log.d("TitlePageAdapter", subCollections.sub_collections.toString())
        rootView.rv_page_titles.adapter = adapter
    }

    private fun getBundle(section: String, title: String): Bundle {
        val path = "$MARKET/$section/$title"
        val bundle = Bundle()
        bundle.putString(PATH,path)
        return bundle
    }

}