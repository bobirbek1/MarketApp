package com.idrok.marketapp.model.callFirebase

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.idrok.marketapp.model.Reklama
import com.google.firebase.storage.FirebaseStorage

class CallStorage {

    private val storageRef = FirebaseStorage.getInstance()

    fun getReklamaImageUrl(list: ArrayList<Reklama>,callback:((String,String) -> Unit)){
        for (reklama in list){
            storageRef.getReference(reklama.img_path)
                .downloadUrl
                .addOnSuccessListener {uri ->
                    callback.invoke(uri.toString(),reklama.doc_path)
                    Log.d("getImageUrl","uri:${uri.path}")
                }
                .addOnFailureListener {exeption ->
                    Log.d("downloadUrl","exeption : $exeption")
                }
        }
    }
    fun downloadImages(path: String, ivProduct: ImageView, context: Context) {
        storageRef.reference.child(path)
            .downloadUrl
            .addOnSuccessListener {
                Glide.with(context)
                    .load(it)
                    .into(ivProduct)
            }
    }
    fun getImageUrl(list:ArrayList<String>,callback:((String) -> Unit)){
        for (img in list){
            storageRef.getReference(img)
                .downloadUrl
                .addOnSuccessListener {uri ->
                    callback.invoke(uri.toString())
                    Log.d("getImageUrl","uri:${uri.path}")
                }
                .addOnFailureListener {exeption ->
                    Log.d("downloadUrl","exeption : $exeption")
                }
        }
    }

}