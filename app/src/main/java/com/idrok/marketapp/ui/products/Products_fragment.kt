package com.idrok.marketapp.ui.products

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.ui.home.QUERY
import com.idrok.marketapp.ui.products.adapter.ProductAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_products.view.*

@RequiresApi(Build.VERSION_CODES.M)
class ProductsFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_products, container, false)
        query = arguments?.getString(QUERY)!!
        Log.d("query", "query:$query")
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setRv()
    }

    private fun setRv() {
        val itemClickListener: ((Product) -> Unit) = { item ->
            val bundle = setArgs(item)
            findNavController().navigate(R.id.productDescription,bundle)
        }
        val ref = FirebaseFirestore.getInstance().collection(query)
        val config = getConfig()
        val options = getOptions(config, ref)
        val adapter = ProductAdapter(itemClickListener, options)
        rootView.rv_products.layoutManager = GridLayoutManager(context, 2)
        rootView.rv_products.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    requireActivity().fab.hide()
                }
                if (dy < 0) {
                    requireActivity().fab.show()
                }
            }

        })
        rootView.rv_products.adapter = adapter
    }

    private fun setArgs(product: Product): Bundle {
        val jsonString = Gson().toJson(product)
        val bundle = Bundle()
        bundle.putString(PRODUCT,jsonString)
        return bundle
    }

    private fun getOptions(
        config: PagedList.Config,
        ref: CollectionReference
    ): FirestorePagingOptions<Product> = FirestorePagingOptions.Builder<Product>()
        .setLifecycleOwner(this)
        .setQuery(ref, config, Product::class.java)
        .build()

    private fun getConfig(): PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(10)
        .setPageSize(10)
        .build()
}