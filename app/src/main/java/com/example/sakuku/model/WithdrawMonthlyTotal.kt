package com.example.sakuku.model

import com.google.gson.annotations.SerializedName

data class WithdrawMonthlyTotal(
    @field:SerializedName("total_withdraw")
    val total_withdraw: String? = null
)

