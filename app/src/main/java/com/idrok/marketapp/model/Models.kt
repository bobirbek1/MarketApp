package com.idrok.marketapp.model

import com.google.firebase.Timestamp


data class Product(
    val doc_path: String = "",
    val name: String = "",
    val company_name: String = "",
    val description: String = "",
    val cost: String = "",
    val available_products: Int = 0,
    val image_path: ArrayList<String> = arrayListOf(),
    val last_update: Timestamp? = null,
    val views: Int = 0,
    val warranty: Int = 0
)

data class SubCollections(
    val sub_collections: ArrayList<String> = arrayListOf(),
    val img_path: String = ""
)


data class Comments(
    val user_name: String = "",
    val comment: String = "",
    val user_color: Int = 0,
    val date: Timestamp? = null
)
data class Reklama(
    val img_path: String = "",
    val doc_path: String = ""
)
data class News(
    val title:String = "",
    val content:String = "",
    val body:String = "",
    val last_update: Timestamp? = null,
    val doc_path: String = "",
    val img_path: String = "",
    val product_path:String = ""
)