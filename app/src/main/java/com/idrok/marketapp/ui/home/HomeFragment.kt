package com.idrok.marketapp.ui.home


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.Reklama
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.model.callFirebase.CallFirestore
import com.idrok.marketapp.model.callFirebase.CallStorage
import com.idrok.marketapp.ui.home.adapter.SectionAdapter
import com.idrok.marketapp.ui.home.adapter.ViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idrok.marketapp.model.newsBellMaker.NewsBellMaker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.math.abs

const val LIST_SUBCOLLECTIONS = "subCollections"
const val LIST_SECTIONS = "listSections"
const val QUERY = "query"

@RequiresApi(Build.VERSION_CODES.M)
class HomeFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var navController: NavController
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var prefs: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var callFirestore: CallFirestore
    private lateinit var callStorage: CallStorage
    private var imageList = arrayListOf<SlideModel>()
    private var listReklama = arrayListOf<Reklama>()
    private val listDocPath = arrayListOf<String>()
    private lateinit var bellMaker:NewsBellMaker


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().nav_view.setItemSelected(R.id.homeFragment,true)
    }

    override fun onResume() {
        super.onResume()
//        requireActivity().invalidateOptionsMenu()
        setVariables()
        viewsVisibility()
        setRv()
        getReklama()
    }

    private fun setVariables() {
        navController = findNavController()
        prefs =
            requireActivity().application.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        gson = Gson()
        callFirestore = CallFirestore(requireContext())
        callStorage = CallStorage()

    }

    private fun getReklama() {
        callFirestore.getReklama { list ->
            listReklama = list
            val listImgPath = arrayListOf<String>()
            for (reklama in listReklama) {
                listImgPath.add(reklama.img_path)
            }
            getImageUrl(listImgPath)

        }
    }

    private fun getImageUrl(listImgPath: ArrayList<String>) {
        if (listImgPath.isNotEmpty()) {
            callStorage.getReklamaImageUrl(listReklama) { img_path, docPath ->
                if (img_path.isNotEmpty() && img_path.isNotBlank())
                    setImageSlider(img_path, docPath)
                else
                    setImageSlider(null, docPath)
            }
        }
    }

    private fun setImageSlider(imgPath: String?, docPath: String) {
        if (imgPath != null) {
            imageList.add(SlideModel(imgPath))
            listDocPath.add(docPath)
            rootView.home_image_slider.setImageList(imageList)
            rootView.home_image_slider.setItemClickListener(object : ItemClickListener {
                override fun onItemSelected(position: Int) {
                    val bundle = Bundle()
                    bundle.putString("doc_path",listDocPath[position])
                    findNavController().navigate(R.id.productDescription,bundle)
                }

            })
        }
//        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
//            imageList = arrayListOf()
//        }
    }


    private fun viewsVisibility() {
        rootView.app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                requireActivity().fab.show()
            } else if (appBarLayout != null && abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                requireActivity().fab.hide()
            } else {
                requireActivity().fab.show()
            }
        })
        requireActivity().cl.visibility = View.VISIBLE
        requireActivity().nav_view.visibility = View.VISIBLE
    }

    private fun setViewPager(
        listTitles: ArrayList<String>,
        section: String
    ) {
        Log.d("setViewPager", "1:$listTitles")
        val viewPagerAdapter = ViewPagerAdapter(this, listTitles, section)
        tabLayout = rootView.tabs_home
        viewPager = rootView.findViewById(R.id.vp_home)
        viewPager.adapter = viewPagerAdapter
        Log.d("setViewPager", "2:$listTitles")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listTitles[position]
        }.attach()

    }


    private fun setRv() {
        rootView.rv_section.setHasFixedSize(true)
        val listSections = getListSections()
        val listSubCollections = getListSubCollections()
        val adapter =
            SectionAdapter(requireContext(), listSections, listSubCollections) { position ->
                setViewPager(listSubCollections[position].sub_collections, listSections[position])
            }
        rootView.rv_section.adapter = adapter
        setViewPager(listSubCollections[0].sub_collections, listSections[0])
    }

    private fun getListSubCollections(): ArrayList<SubCollections> {
        val string = prefs.getString(LIST_SUBCOLLECTIONS, "")
        val type = object : TypeToken<ArrayList<SubCollections>>() {}.type
        return gson.fromJson(string, type)

    }

    private fun getListSections(): ArrayList<String> {
        val string = prefs.getString(LIST_SECTIONS, "")
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(string, type)
    }

    override fun onStop() {
        super.onStop()
        imageList = arrayListOf()
    }

    override fun onPause() {
        super.onPause()
        imageList = arrayListOf()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach","onDetach")
        imageList = arrayListOf()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDetach","onDestroy")
        imageList = arrayListOf()
    }
}

