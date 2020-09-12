package com.idrok.marketapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.model.newsBellMaker.NewsBellMaker
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fab_dialog.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var prefs: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var bellMaker: NewsBellMaker
    private var lastBackPressed: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = this.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        gson = Gson()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        getNotificationData()
        val navView: ChipNavigationBar = findViewById(R.id.nav_view)
        setSupportActionBar(main_toolbar)
        val window = this.window
        window.statusBarColor = this.getColor(R.color.colorPrimaryDark)
        bellMaker = NewsBellMaker(this, navController)
        setNavOnClick(navView, navController)
    }


    private fun getNotificationData() {
        val bundle = Bundle()
        if (intent.hasExtra("DOC_PATH")) {
            val docPath = intent.getStringExtra("DOC_PATH")
            bundle.putString("DOC_PATH", docPath)
            if (intent.hasExtra("PRODUCT_PATH")) {
                bundle.putString("PRODUCT_PATH", intent.getStringExtra("PRODUCT_PATH"))
            }
            navController.navigate(R.id.newsPage, bundle)
        }
    }

    private fun setNavOnClick(
        navView: ChipNavigationBar,
        navController: NavController
    ) {
        navView.setItemSelected(R.id.homeFragment, true)
        navView.setOnItemSelectedListener {
            navController.navigate(it)
        }
    }

    override fun onResume() {
        super.onResume()
        nav_view.visibility = View.VISIBLE
        fab.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        val view = LayoutInflater.from(this)
            .inflate(R.layout.fab_dialog, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        view.cv_fab_tel_1.setOnClickListener {
            callPhone(R.string.tel_2)
        }
        view.cv_fab_tel_2.setOnClickListener {
            callPhone(R.string.tel_3)
        }
        view.btn_fab.setOnClickListener {
            dialog.dismiss()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_activity, menu)
        if (menu != null)
            bellMaker.onCreateBellIcon(menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.telegram) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/joinchat/AAAAAFP1CSL0qtHxSMJ4MA")
            )
            startActivity(intent)
        }
        if (item.itemId == R.id.share_icon) {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Chooser title")
                .setText("http://play.google.com/store/apps/details?id=" + this.packageName)
                .startChooser()
        }
        return true
    }

    override fun onBackPressed() {
        Log.d("MyMainActivity", "onBackPressed")
        val dest = navController.currentDestination?.id
        if (dest == R.id.homeFragment) {
            if (doubleClicked()) {
                finishAffinity()
            }
        } else if (dest == R.id.sectionsFragment) {
            if (doubleClicked())
                finishAffinity()
        } else if (dest == R.id.settingsFragment) {
            if (doubleClicked())
                finishAffinity()
        } else {
            super.onBackPressed()
        }

    }

    private fun doubleClicked(): Boolean {
        Log.d("MyMainActivity", "$lastBackPressed")
        return if (lastBackPressed + 2000 > System.currentTimeMillis()) {
            Log.d("MyMainActivity", "true:$lastBackPressed")
            true
        } else {
            Log.d("MyMainActivity", "false$lastBackPressed")
            lastBackPressed = System.currentTimeMillis()
            Toast.makeText(this, getString(R.string.onBackPress), Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun callPhone(tel: Int) {
        val uri = Uri.parse("tel:${getString(tel)}")
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
    }
}
