package com.example.sakuku.network

import com.example.sakuku.model.MonthlyTotal
import com.example.sakuku.model.ResponseUser
import com.example.sakuku.model.Transaction
import com.example.sakuku.model.WithdrawMonthlyTotal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/transactions/total-amount/")
    suspend fun getTotalAmount(): Response<ResponseUser>

    @GET("api/transactions/total-add-monthly/")
    suspend fun addTotalAmount(): Response<List<MonthlyTotal>>

    @GET("api/transactions/total-withdraw-monthly")
    suspend fun withdrawTotalAmount(): Response<List<WithdrawMonthlyTotal>>

    @POST("api/transactions")
    suspend fun addTransaction(@Body transaction: Transaction): Response<Void> // Ganti Void dengan tipe response yang sesuai jika API mengembalikan data
}