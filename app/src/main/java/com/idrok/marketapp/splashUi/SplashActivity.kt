package com.idrok.marketapp.splashUi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.idrok.marketapp.IS_USER_REGISTRATE
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.SubCollections
import com.idrok.marketapp.model.callFirebase.CallFirestore
import com.idrok.marketapp.ui.MainActivity
import com.idrok.marketapp.ui.home.LIST_SECTIONS
import com.idrok.marketapp.ui.home.LIST_SUBCOLLECTIONS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

const val NEWS_DOC_PATH = "NEWS_DOC_PATH"

class SplashActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var prefs: SharedPreferences
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_splash_fragment) as NavHostFragment
        navController = navHostFragment.navController
        prefs = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        gson = Gson()
        checkUserLogin()
        onOpenSplash()

    }

    private fun onOpenSplash() {
        val callFirestore = CallFirestore(this)
        val mIntent = Intent(this, MainActivity().javaClass)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("onDestinationChanged", "destination:$destination")
            if (destination.id == R.id.splashFragment) {
                callFirestore.getSectionsAndSubCollections { listSections, listSubCollections ->
                    save(listSections, listSubCollections)
                    callFirestore.getNewsDocPath {
                        savePrefs(it)
                        startActivity(mIntent)
                    }

                }
            }
        }
    }

    private fun save(
        listSections: ArrayList<String>,
        listSubCollections: ArrayList<SubCollections>
    ) {
        val sectionString = gson.toJson(listSections)
        val subCollectionString = gson.toJson(listSubCollections)
        prefs.edit()
            .putString(LIST_SECTIONS, sectionString)
            .putString(LIST_SUBCOLLECTIONS, subCollectionString)
            .apply()
    }

    private fun savePrefs(list: ArrayList<String>) {
        Log.d("SplashActivity","list: $list")
        var jsonString = ""
        val string = prefs.getString(NEWS_DOC_PATH, "")
        if (!string.isNullOrEmpty()) {
            Log.d("SplashActivity","stringNotNull: $string")
            val type = object : TypeToken<HashMap<String, Boolean>>() {}.type
            val hashMap = gson.fromJson<HashMap<String, Boolean>>(string, type)
            for (doc_path in list) {
                if (hashMap != null) {
                    if (!hashMap.containsKey(doc_path)) {
                        hashMap[doc_path] = false
                    }
                }
            }
            Log.d("SplashActivity","hashMap:$hashMap")
            jsonString = gson.toJson(hashMap)
        } else {
            Log.d("SplashActivity","stringNull")
            val hashMap = hashMapOf<String, Boolean>()
            for (doc_path in list) {
                hashMap[doc_path] = false
            }
            Log.d("SplashActivity","hashMap: $hashMap")
            jsonString = gson.toJson(hashMap)
        }
        if (jsonString.isNotEmpty()) {
            prefs.edit()
                .putString(NEWS_DOC_PATH, jsonString)
                .apply()
        }
    }

    private fun checkUserLogin() {
        val registrate = prefs.getBoolean(IS_USER_REGISTRATE, false)
        if (!registrate) {
            navController.navigate(R.id.registrationFragment)
        }
    }


}