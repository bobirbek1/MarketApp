package com.idrok.marketapp.ui.home.adapter


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idrok.marketapp.model.callFirebase.MARKET
import com.idrok.marketapp.ui.home.QUERY
import com.idrok.marketapp.ui.products.ProductsFragment


class ViewPagerAdapter(
    fragment: Fragment,
    private val listTitles: ArrayList<String>,
    private val section: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = listTitles.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ProductsFragment()
        val query = "$MARKET/$section/${listTitles[position]}"
        val bundle = Bundle()
            bundle.putString(QUERY,query)
        fragment.arguments = bundle
        return fragment
    }
}
