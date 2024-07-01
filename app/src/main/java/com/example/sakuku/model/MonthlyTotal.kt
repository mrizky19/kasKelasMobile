package com.example.sakuku.model

import com.google.gson.annotations.SerializedName

data class MonthlyTotal(
    @field:SerializedName("total_add")
    val total_Add: String? = null
)

