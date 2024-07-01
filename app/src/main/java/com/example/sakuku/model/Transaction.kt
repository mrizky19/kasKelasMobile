package com.example.sakuku.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    val category: String,
    val description: String,
    val date_update: String, // Pastikan format tanggal sesuai dengan yang diharapkan API
    val user: String,
    val add_transaction: Int,
    val withdraw_transaction: Int
)
