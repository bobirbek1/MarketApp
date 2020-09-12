package com.idrok.marketapp.ui.products.productDescription

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Comments
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.model.callFirebase.CallFirestore
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.idrok.marketapp.splashUi.registration.PROFILE_COLOR
import com.idrok.marketapp.splashUi.registration.PROFILE_NAME
import com.idrok.marketapp.splashUi.registration.PROFILE_SURNAME
import com.idrok.marketapp.ui.products.PRODUCT
import com.idrok.marketapp.ui.products.adapter.CommentAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product_description.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProductDescription : Fragment() {

    private lateinit var rootView: View
    private val imageList = ArrayList<SlideModel>()
    private val callStorage = CallStorage()
    private lateinit var prefs: SharedPreferences
    private lateinit var callFirestore: CallFirestore
    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_product_description, container, false)
        return rootView
    }


    override fun onResume() {
        super.onResume()
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        callFirestore = CallFirestore((requireContext()))
        if (!requireArguments().isEmpty) {
            val product = requireArguments().getString(PRODUCT)
            val docPath = requireArguments().getString("doc_path")
            if (product != null)
                getProduct(requireArguments().getString(PRODUCT)!!)
            else if (docPath != null)
                getProductsFromServer(docPath)
            setViews()
        }
    }

    private fun getProductsFromServer(docPath: String) {
            callFirestore = CallFirestore(requireContext())
        callFirestore.getProduct(docPath){product ->
            updateViews(product)
            getImageUrl(product.image_path)
            setAllElements(product)
            setUpCommentsRv(product.doc_path)
            setComments(product.doc_path)
        }
    }

    private fun setComments(docPath: String) {
        rootView.et_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (rootView.et_comment.text!!.isNotEmpty()) {
                    rootView.iv_send.isClickable = true
                    sendComment(docPath)
                } else {
                    rootView.iv_send.isClickable = false
                }
            }
        })
    }

    private fun sendComment(docPath: String) {
        rootView.iv_send.setOnClickListener {
            val text = rootView.et_comment.text
            if (!text.isNullOrEmpty()) {
                val name = prefs.getString(PROFILE_NAME, "")
                val surname = prefs.getString(PROFILE_SURNAME, "")
                val color = prefs.getInt(PROFILE_COLOR, 0)
                val hashMap: HashMap<String, Any> = hashMapOf(
                    "user_name" to "$name $surname",
                    "comment" to text.toString(),
                    "user_color" to color,
                    "date" to Date()
                )
                callFirestore.saveComments(hashMap, docPath)
                rootView.et_comment.setText("")
                val imm =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }
        }
    }

    private fun getProduct(jsonString: String) {
        val type = object : TypeToken<Product>() {}.type
        val product = Gson().fromJson<Product>(jsonString, type)
        updateViews(product)
        getImageUrl(product.image_path)
        setAllElements(product)
        setUpCommentsRv(product.doc_path)
        setComments(product.doc_path)
    }

    private fun updateViews(product: Product) {
        callFirestore.updateViews(product.doc_path, product.views + 1)
    }

    private fun getImageUrl(imagePath: ArrayList<String>) {
        if (imagePath.isNotEmpty()) {
            callStorage.getImageUrl(imagePath) { url ->
                setImageSlider(url)
            }
        } else {
            setImageSlider(null)
        }

    }

    private fun setAllElements(product: Product) {
        val lastUpdate = simpleDateFormat.format(product.last_update!!.toDate())

        rootView.tv_Title.text = product.name
        rootView.tv_company_name.text = product.company_name
        rootView.tv_cost.text = product.cost
        rootView.tv_last_update.text = lastUpdate
        rootView.tv_available.text = product.available_products.toString()
        rootView.tv_views.text = product.views.toString()
        rootView.tv_full_description.text = product.description

    }


    private fun setImageSlider(imagePath: String?) {
        if (imagePath != null) {
            imageList.add(SlideModel(imagePath))
            rootView.product_image_slider.setImageList(imageList)
        } else {
            imageList.add(SlideModel(R.drawable.place_holder))
        }
    }

    private fun setUpCommentsRv(docPath: String) {
        val path = "$docPath/Comments"
        val options = getOptions(path)
        val adapter = CommentAdapter(options)
        rootView.rv_comments.layoutManager = LinearLayoutManager(requireContext())
        rootView.rv_comments.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                    requireActivity().fab.hide()
                } else {
                    requireActivity().fab.show()
                }
            }
        })
        rootView.rv_comments.adapter = adapter

    }

    private fun getOptions(path: String): FirestoreRecyclerOptions<Comments> {
        val query = FirebaseFirestore.getInstance().collection(path)
            .orderBy("date", Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<Comments>()
            .setLifecycleOwner(this)
            .setQuery(query, Comments::class.java)
            .build()
    }


    private fun setViews() {
        requireActivity().cl.visibility = View.VISIBLE
        requireActivity().nav_view.visibility = View.GONE
        requireActivity().fab.show()
        setMarginFab(72)
    }

    private fun setMarginFab(dp: Int) {
        val marginInDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources
                .displayMetrics
        ).toInt()
        val params = requireActivity().fab.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = marginInDp
    }

    override fun onDetach() {
        super.onDetach()
        setMarginFab(16)
    }

}

