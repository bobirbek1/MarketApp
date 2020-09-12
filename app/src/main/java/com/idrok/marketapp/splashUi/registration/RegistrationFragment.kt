package com.idrok.marketapp.splashUi.registration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idrok.marketapp.IS_USER_REGISTRATE
import com.idrok.marketapp.PREFERENCE_KEY
import com.idrok.marketapp.R
import kotlinx.android.synthetic.main.fragment_registration.view.*
import java.util.*

const val PROFILE_NAME = "profile_name"
const val PROFILE_SURNAME = "profile_surname"
const val PROFILE_COLOR = "profile_color"
class RegistrationFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_registration, container, false)
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setAvatarColor()
        setReg()
    }

    private fun setReg() {
        rootView.btn_reg.setOnClickListener {
            if (setEditText()){
                val name = rootView.et_reg_name.text.toString()
                val surname = rootView.et_reg_surname.text.toString()
                prefs.edit()
                    .putString(PROFILE_NAME,name)
                    .putString(PROFILE_SURNAME,surname)
                    .putBoolean(IS_USER_REGISTRATE,true)
                    .apply()
                findNavController().navigate(R.id.splashFragment)
            }
        }
    }

    private fun setEditText() : Boolean {
        val name = rootView.et_reg_name
        val surname = rootView.et_reg_surname
        name.addTextChangedListener(object : TextWatcher{
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
        surname.addTextChangedListener(object : TextWatcher{
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

    private fun setAvatarColor() {
        val list = arrayListOf<Int>()
        addColor(list)
        val pos = (0..6).random()
        prefs.edit()
            .putInt(PROFILE_COLOR,list[pos])
            .apply()
        rootView.cv_reg_avatar.background.setTint(list[pos])
    }

    private fun addColor(list: ArrayList<Int>) {
        list.add(requireActivity().getColor(R.color.red500))
        list.add(requireActivity().getColor(R.color.pink500))
        list.add(requireActivity().getColor(R.color.indigo500))
        list.add(requireActivity().getColor(R.color.cyan500))
        list.add(requireActivity().getColor(R.color.green500))
        list.add(requireActivity().getColor(R.color.yellow500))
        list.add(requireActivity().getColor(R.color.brown500))
    }

}