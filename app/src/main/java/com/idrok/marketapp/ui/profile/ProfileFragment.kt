package com.idrok.marketapp.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idrok.marketapp.IS_USER_REGISTRATE
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import com.idrok.marketapp.splashUi.registration.PROFILE_COLOR
import com.idrok.marketapp.splashUi.registration.PROFILE_NAME
import com.idrok.marketapp.splashUi.registration.PROFILE_SURNAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_registration.view.*


class ProfileFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var prefs:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_registration, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        setViews()
    }

    private fun setViews() {
        requireActivity().fab.hide()
        requireActivity().cl.visibility = View.VISIBLE
        requireActivity().nav_view.visibility = View.GONE
        setAvatar()
        setReg()
    }

    private fun setAvatar() {
        val color = prefs.getInt(PROFILE_COLOR,0)
        Log.d("ProfileFragment","setAvatar:$color")
        if (color != 0){
            rootView.cv_reg_avatar.setCardBackgroundColor(color)
        }
    }
    private fun setReg() {
        val boolean = setEditText()
        rootView.btn_reg.setOnClickListener {
            if (boolean){
                val name = rootView.et_reg_name.text.toString()
                val surname = rootView.et_reg_surname.text.toString()
                Log.d("ProfileFragment","setName:$name")
                Log.d("ProfileFragment","setName:$surname")
                prefs.edit()
                    .putString(PROFILE_NAME,name)
                    .putString(PROFILE_SURNAME,surname)
                    .putBoolean(IS_USER_REGISTRATE,true)
                    .apply()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setEditText() : Boolean {
        val name = rootView.et_reg_name
        val surname = rootView.et_reg_surname
        val stringName = prefs.getString(PROFILE_NAME,"")
        val stringSurName = prefs.getString(PROFILE_SURNAME,"")
        Log.d("ProfileFragment","getName:$stringName")
        Log.d("ProfileFragment","getName:$stringSurName")
        name.setText(stringName)
        surname.setText(stringSurName)
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    name.error = null
                } else {
                    name.error = "Ismingizni kiriting"
                }
            }

        })
        surname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    surname.error = null
                }else {
                    surname.error = "Familiyangizni kiriting"
                }
            }

        })
        return if (!name.text.isNullOrEmpty()){
            if (!surname.text.isNullOrEmpty()){
                true
            } else {
                surname.error = "Familiyangizni kiriting"
                false
            }
        } else {
            name.error = "Ismingizni kiritng"
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().fab.visibility = View.VISIBLE
    }
}


