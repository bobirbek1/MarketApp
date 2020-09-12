package com.idrok.marketapp.model.callFirebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.idrok.marketapp.model.News
import com.idrok.marketapp.model.Product
import com.idrok.marketapp.model.Reklama
import com.idrok.marketapp.model.SubCollections
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query


const val MARKET = "market"
const val REKLAMA = "reklama"
class CallFirestore(private val context: Context) {
    private  var firestore :FirebaseFirestore

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = settings
    }

    fun getSectionsAndSubCollections(callBack: (ArrayList<String>,ArrayList<SubCollections>) -> Unit){
        val list = arrayListOf<String>()
        val listSubCollections = arrayListOf<SubCollections>()
        firestore.collection(MARKET)
            .get()
            .addOnSuccessListener { listDocuments ->
                for (document in listDocuments){
                    list.add(document.id)
                    Log.d("getSections","docRef:${document.reference.path}")
                    listSubCollections.add(document.toObject(SubCollections::class.java))
                    Log.d("getData","sections$document")
                }

                callBack.invoke(list,listSubCollections)
            }
            .addOnFailureListener {
                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show()
                Log.e("getSections","${it.message}")
            }
    }

    fun updateViews(docPath:String,views:Int){
        firestore.document(docPath)
            .update("views",views)
            .addOnSuccessListener {
                Log.d("updateViews","successful updated")
            }
            .addOnFailureListener {
                Log.d("updateViews","$it")
            }
    }
    fun saveComments(map:HashMap<String,Any>,path:String){
        val docPath = "$path/Comments"
        firestore.collection(docPath).document()
            .set(map)
            .addOnSuccessListener {
                Log.d("saveComments","comments saved successful")
            }
            .addOnFailureListener {
                Log.d("saveComments","comments save failed")
            }
    }
    fun getReklama(callBack: (ArrayList<Reklama>) -> Unit){
        val list = arrayListOf<Reklama>()
        firestore.collection(REKLAMA)
            .orderBy("last_update",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    list.add(document.toObject(Reklama::class.java))
                    Log.d("getReklama","get reklama success")
                }
                callBack.invoke(list)
            }
            .addOnFailureListener {
                Log.d("getReklama","get reklama failed: ${it.message}")
            }

    }
    fun getProduct(docPath: String,callBack: (Product) -> Unit){
        firestore.document(docPath)
            .get()
            .addOnSuccessListener {product ->
                if (product.exists())
                callBack.invoke(product.toObject(Product::class.java)!!)
            }
            .addOnFailureListener {
                Log.e("getProducts","error:${it.message}")
            }
    }
    fun getNewsDocPath(callBack: (ArrayList<String>) -> Unit){
        val listDocPath = arrayListOf<String>()
        firestore.collection("yangiliklar")
            .orderBy("last_update",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    listDocPath.add(doc.toObject(News::class.java).doc_path)
                }
                callBack.invoke(listDocPath)
            }
            .addOnFailureListener{
                Log.d("getNews","$it.message")
            }
    }
    fun getNews(docPath: String,callBack: (News) -> Unit){
        firestore.document("yangiliklar/$docPath")
            .get()
            .addOnSuccessListener {
                if (it.exists()){
                callBack.invoke(it.toObject(News::class.java)!!)}
            }
            .addOnFailureListener {
                Log.e("getNews","${it.message}")
            }
    }

}