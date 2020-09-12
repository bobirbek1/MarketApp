package com.idrok.marketapp.model.newsBellMaker

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.splashUi.NEWS_DOC_PATH
import kotlinx.android.synthetic.main.notification_bell.view.*

class NewsBellMaker(activity: Activity, private val navController: NavController) {
    private var badge = 0
    private val prefs = activity.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    fun onCreateBellIcon(menu: Menu) {
        getBadges()
        val bell = menu.findItem(R.id.news_bell)
        if (bell != null) {
            Log.d("MainActivity", "onCreateOptionsMenu:$bell")
            bell.setActionView(R.layout.notification_bell)
            val counter = bell.actionView.cart_badge
            if (badge == 0) {
                counter.visibility = View.GONE
            } else {
                if (badge > 9) {
                    counter.visibility = View.VISIBLE
                    counter.text = "9+"
                } else {
                    counter.visibility = View.VISIBLE
                    counter.text = badge.toString()
                }
            }
            bell.actionView.setOnClickListener {
                navController.navigate(R.id.newsFragment)
            }
        }
    }

    private fun getBadges() {
        badge = 0
        val type = object : TypeToken<HashMap<String, Boolean>>() {}.type
        val string = prefs.getString(NEWS_DOC_PATH, "")
        Log.d("MainActivity", "getBadges: $string")

        if (!string.isNullOrEmpty()) {
            val hashMap = Gson().fromJson<HashMap<String, Boolean>>(string, type)
            Log.d("MainActivity", "hashMap: $hashMap")
            if (hashMap != null) {
                for (keys in hashMap.keys) {
                    Log.d("MainActivity", "keys:$keys")
                    if (!hashMap[keys]!!) {
                        badge++
                    }
                }
            }
        }
    }

}