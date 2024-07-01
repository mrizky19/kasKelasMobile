package com.example.sakuku.model

import com.google.gson.annotations.SerializedName

data class ResponseUser(
    @field:SerializedName("total_amount")
    val total_amount: String? = null
)

