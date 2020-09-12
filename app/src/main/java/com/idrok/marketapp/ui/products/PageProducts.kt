package com.idrok.marketapp.ui.products

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.ui.products.adapter.ProductsPageAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_page_products.view.*
import java.util.ArrayList

const val PRODUCT = "product"
const val FILTER = "filter"

class PageProducts : Fragment() {

    private lateinit var rootView: View
    private lateinit var gson: Gson
    private val firestore = FirebaseFirestore.getInstance()
    private var listFilter = arrayListOf(false, false, false, false)
    private lateinit var config: PagedList.Config
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_page_products, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        config = getConfig()
        gson = Gson()
        setViews()
        if (!requireArguments().isEmpty)
            setRv(requireArguments().getString(PATH)!!)
    }


    private fun setRv(path: String) {
        val query = firestore.collection(path).orderBy("last_update",Query.Direction.DESCENDING)
        listFilter = getLastFilter()
        Log.d("getLastFilter","listFilter:$listFilter")
        val options = getOptions(config,query)
        val onItemClickListener: ((Product) -> Unit) = { product ->
            val bundle = setArgs(product)
            findNavController().navigate(R.id.productDescription, bundle)
        }
        val adapter = ProductsPageAdapter(onItemClickListener, options)
        setChipClickListener(adapter,path)
        rootView.rv_page_products.adapter = adapter
        changeQuery(adapter,path)
    }

    private fun getLastFilter(): ArrayList<Boolean> {
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        val jsonString = prefs.getString(FILTER,"")
        val type = object : TypeToken<ArrayList<Boolean>>() {}.type
        return if (jsonString != null && jsonString.isNotEmpty()){
            gson.fromJson(jsonString,type)
        } else {
            arrayListOf(true,false,false,false)
        }
    }

    private fun setChipClickListener(
        adapter: ProductsPageAdapter,
        path: String
    ) {
        val arzon = rootView.chip_arzonroq
        val qimmat = rootView.chip_qimatroq
        val korilgan = rootView.chip_korilgan
        val yangi = rootView.chip_yangi

        arzon.setOnClickListener {
            arzon.isCheckedIconVisible = arzon.isChecked
            listFilter = arrayListOf(false,false,false,false)
            listFilter[1] = arzon.isChecked
            changeQuery(adapter, path)
        }
        qimmat.setOnClickListener {
            qimmat.isCheckedIconVisible = qimmat.isChecked
            listFilter = arrayListOf(false,false,false,false)
            listFilter[2] = qimmat.isChecked
            changeQuery(adapter, path)
        }
        korilgan.setOnClickListener {
            korilgan.isCheckedIconVisible = korilgan.isChecked
            listFilter = arrayListOf(false,false,false,false)
            listFilter[3] = korilgan.isChecked
            changeQuery(adapter, path)
        }
        yangi.setOnClickListener {
            yangi.isCheckedIconVisible= yangi.isChecked
            listFilter = arrayListOf(false,false,false,false)
            listFilter[0] = yangi.isChecked
            changeQuery(adapter, path)
        }
        Log.d("setChipClickListener","list $listFilter")
    }

    private fun changeQuery(adapter: ProductsPageAdapter, path: String) {
        (0..listFilter.size).forEach { position ->
            when (position) {
                0 -> {
                    rootView.chip_yangi.isCheckedIconVisible = rootView.chip_yangi.isChecked
                    if (listFilter[0]) {
                        val mQuery = firestore.collection(path).orderBy("last_update",Query.Direction.DESCENDING)
                        adapter.updateOptions(getOptions(getConfig(),mQuery))
                    }
                }
                1 -> {
                    rootView.chip_arzonroq.isCheckedIconVisible = rootView.chip_arzonroq.isChecked
                    if (listFilter[1]) {
                        val mQuery = firestore.collection(path).orderBy("cost",Query.Direction.ASCENDING)
                        adapter.updateOptions(getOptions(getConfig(),mQuery))
                    }
                }
                2 -> {
                    rootView.chip_qimatroq.isCheckedIconVisible = rootView.chip_qimatroq.isChecked
                    if (listFilter[2]) {
                        val mQuery = firestore.collection(path).orderBy("cost",Query.Direction.DESCENDING)
                        adapter.updateOptions(getOptions(getConfig(),mQuery))
                    }
                }
                3 -> {
                    rootView.chip_korilgan.isCheckedIconVisible = rootView.chip_korilgan.isChecked
                    if (listFilter[3]) {
                        val mQuery = firestore.collection(path).orderBy("views",Query.Direction.DESCENDING)
                        adapter.updateOptions(getOptions(getConfig(),mQuery))
                    }
                }
            }
        }
    }

    private fun setArgs(product: Product): Bundle {
        val jsonItem = Gson().toJson(product)
        val bundle = Bundle()
        bundle.putString(PRODUCT, jsonItem)
        return bundle
    }

    private fun getOptions(
        config: PagedList.Config,
        query: Query
    ): FirestorePagingOptions<Product> = FirestorePagingOptions.Builder<Product>()
        .setLifecycleOwner(this)
        .setQuery(query, config, Product::class.java)
        .build()


    private fun getConfig(): PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(10)
        .setPageSize(20)
        .build()

    private fun setViews() {
        requireActivity().cl.visibility = View.VISIBLE
        requireActivity().fab.show()
        requireActivity().nav_view.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val jsonString = gson.toJson(listFilter)
        prefs.edit()
            .putString(FILTER,jsonString)
            .apply()
    }

}