package com.idrok.marketapp.ui.sections

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.ui.home.LIST_SECTIONS
import com.idrok.marketapp.ui.home.LIST_SUBCOLLECTIONS
import com.idrok.marketapp.ui.sections.adapter.SectionAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sections.view.*


const val POSITION = "position"
const val SECTION = "section"

class SectionsFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var prefs: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sections, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().nav_view.setItemSelected(R.id.sectionsFragment, true)
    }

    override fun onResume() {
        super.onResume()
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        setViews()
        getSections()
    }

    private fun getBundle(section: String, position: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(SECTION, section)
        bundle.putInt(POSITION, position)
        return bundle
    }

    private fun getSections() {
        val list = getListSections()
        Log.d("getSectons", "1:$list")
        if (list.isNotEmpty()) {
            Log.d("getSectons", "2:$list")
            setRv(list)
        }
    }

    private fun setRv(list: ArrayList<String>) {
        Log.d("setRv", "$list")
        val listImg = getListSubcollections()
        val adapter = SectionAdapter(requireContext(), list, listImg) { position ->
            val bundle = getBundle(list[position], position)
            findNavController().navigate(R.id.titleFragment, bundle)
        }

        rootView.rv_page_section.layoutManager = GridLayoutManager(requireContext(), 2)
        rootView.rv_page_section.adapter = adapter
    }

    private fun getListSubcollections(): ArrayList<SubCollections> {
        val string = prefs.getString(LIST_SUBCOLLECTIONS, "")
        val type = object : TypeToken<ArrayList<SubCollections>>() {}.type
        return Gson().fromJson(string, type)
    }

    private fun getListSections(): ArrayList<String> {
        val string = prefs
            .getString(LIST_SECTIONS, "")
        val type = object : TypeToken<ArrayList<String>>() {}.type
        val list = Gson().fromJson<ArrayList<String>>(string, type)
        if (list != null) {
            return list
        }
        return arrayListOf()
    }

    private fun setViews() {
        requireActivity().fab.show()
        requireActivity().nav_view.visibility = View.VISIBLE
        requireActivity().cl.visibility = View.VISIBLE
    }

}