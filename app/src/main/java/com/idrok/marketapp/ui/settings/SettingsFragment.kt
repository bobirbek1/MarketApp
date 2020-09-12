package com.idrok.marketapp.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.splashUi.registration.PROFILE_COLOR
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment(), View.OnClickListener {


    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        setViews()
        setHasOptionsMenu(true)
        setOnClicks()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().nav_view.setItemSelected(R.id.settingsFragment, true)
    }

    private fun setOnClicks() {
        rootView.ll_profile_edit.setOnClickListener(this)
        rootView.ll_dastur_haqida.setOnClickListener(this)
//        rootView.ll_tillar.setOnClickListener(this)
        rootView.ll_aloqa.setOnClickListener(this)
        rootView.ll_baholash.setOnClickListener{
            showBaholashDialog()
        }
    }


    private fun setViews() {
        requireActivity().cl.visibility = View.VISIBLE
        requireActivity().nav_view.visibility = View.VISIBLE
        requireActivity().fab.show()
        setAvatar()
    }


    private fun setAvatar() {
        val prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        val color = prefs.getInt(PROFILE_COLOR, 0)
        Log.d("SettingsFragment","setAvatar:$color")
        if (color != 0) {
            rootView.cv_avatar.setCardBackgroundColor(color)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            rootView.ll_profile_edit -> {
                findNavController().navigate(R.id.profileFragment)
            }
            rootView.ll_dastur_haqida -> {
                showAboutDialog()
            }
//            rootView.ll_tillar -> {
//                showLanguageDialog()
//            }
            rootView.ll_aloqa -> {
                showAloqaDialog()
            }
//            rootView.ll_baholash -> {
//                showBaholashDialog()
//            }
        }
    }

    private fun showBaholashDialog() {
        Log.d("SettingsFragment","showReview:open")
        val reviewManager = ReviewManagerFactory.create(requireContext())
        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            Toast.makeText(requireContext(),"request:$request",Toast.LENGTH_SHORT).show()
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                flow.addOnCompleteListener {
                }
            } else {
                Log.d("SettingsFragment", request.exception.toString())
            }
        }
    }

    private fun showAloqaDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val view = requireActivity().layoutInflater
            .inflate(R.layout.dialog_layout, null)
        builder.setView(view)
        view.tv_dialog_body.visibility = View.GONE
        view.cv_tel_1.visibility = View.VISIBLE
        view.cv_email.visibility = View.VISIBLE
        view.cv_rus.visibility = View.GONE
        view.cv_uzbek.visibility = View.GONE


        view.tv_dialog_title.setBackgroundColor(requireActivity().getColor(R.color.red500))
        view.btn_dialog.setBackgroundColor(requireActivity().getColor(R.color.red500))

        view.tv_dialog_title.setText(R.string.dasturchi_bilan_aloqa)
        val dialog = builder.create()
        view.cv_tel_1.setOnClickListener {
            if (isPermissionGranted()) {
               callPhone()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),1)
            }
        }
        view.cv_email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:anvarjonovbob1999@mail.ru"))
            startActivity(intent)
        }
        view.btn_dialog.setOnClickListener {
            dialog.dismiss()
        }
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
    }

    private fun callPhone() {
        val uri = Uri.parse("tel:+998994012741")
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23){
            requireActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

//    private fun showLanguageDialog() {
//        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
//        val view = requireActivity().layoutInflater
//            .inflate(R.layout.dialog_layout, null)
//        builder.setView(view)
//        view.tv_dialog_body.visibility = View.GONE
//        view.cv_tel_1.visibility = View.GONE
//        view.cv_email.visibility = View.GONE
//        view.cv_rus.visibility = View.VISIBLE
//        view.cv_uzbek.visibility = View.VISIBLE
//
//
//        view.tv_dialog_title.setText(R.string.tilni_tanlang)
//        view.tv_dialog_title.setBackgroundColor(requireActivity().getColor(R.color.cyan500))
//        view.btn_dialog.setBackgroundColor(requireActivity().getColor(R.color.cyan500))
//
//        val dialog = builder.create()
//        view.cv_uzbek.setOnClickListener {
//            LocaleHelper().setLocale(requireActivity(), "uz")
//            dialog.dismiss()
//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//        }
//        view.cv_rus.setOnClickListener {
//            LocaleHelper().setLocale(requireActivity(), "ru")
//            dialog.dismiss()
//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        view.btn_dialog.setOnClickListener {
//            dialog.dismiss()
//        }
//        if (dialog.window != null) {
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
//        }
//        dialog.show()
//    }

    private fun showAboutDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val view = requireActivity().layoutInflater
            .inflate(R.layout.dialog_layout, null)
        builder.setView(view)
        view.tv_dialog_body.visibility = View.VISIBLE
        view.cv_tel_1.visibility = View.GONE
        view.cv_email.visibility = View.GONE
        view.cv_rus.visibility = View.GONE
        view.cv_uzbek.visibility = View.GONE


        view.tv_dialog_title.setText(R.string.dastur_haqida)
        view.tv_dialog_body.setText(R.string.about_app)
        view.btn_dialog.setBackgroundColor(requireActivity().getColor(R.color.green500))
        val dialog = builder.create()

        view.btn_dialog.setOnClickListener {
            dialog.dismiss()
        }
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPhone()
            } else {
                Toast.makeText(requireContext(),R.string.permission,Toast.LENGTH_SHORT).show()
            }
        }
    }


}