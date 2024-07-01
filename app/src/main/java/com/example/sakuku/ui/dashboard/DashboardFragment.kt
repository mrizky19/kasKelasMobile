package com.example.sakuku.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sakuku.databinding.FragmentDashboardBinding
import com.example.sakuku.model.ResponseUser
import com.example.sakuku.network.ApiConfig
import com.example.sakuku.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi ApiService
        apiService = ApiConfig().getApiService()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari API
        lifecycleScope.launch {
            try {
                val responseTotalAmount = apiService.getTotalAmount()
                val responseAddMonthly = apiService.addTotalAmount()
                val responseWithdrawMonthly = apiService.withdrawTotalAmount()

                if (responseTotalAmount.isSuccessful && responseAddMonthly.isSuccessful) {
                    val totalAmount = responseTotalAmount.body()?.total_amount ?: "0"
                    val totalAddMonthly = responseAddMonthly.body()?.let { monthlyTotals ->
                        monthlyTotals.sumOf { it.total_Add?.toIntOrNull() ?: 0 }
                    }.toString()
                    val totalWithdrawMonthly = responseWithdrawMonthly.body()?.let { WithdrawMonthly ->
                        WithdrawMonthly.sumOf { it.total_withdraw?.toIntOrNull() ?: 0 }
                    }.toString()

                    // Update TextView di Main Thread
                    withContext(Dispatchers.Main) {
                        binding.cardTotalUangKas.totalAmount.text = "Rp $totalAmount"
                        binding.cardPemasukan.totalAddMonthly.text = "Rp $totalAddMonthly"
                        binding.cardPengeluaran.totalWithdrawMonthly.text = "Rp $totalWithdrawMonthly"
                    }
                } else {
                    // Log error yang lebih spesifik
                    if (!responseTotalAmount.isSuccessful) {
                        Log.e("DashboardFragment", "Gagal mengambil total amount: ${responseTotalAmount.code()}")
                    }
                    if (!responseAddMonthly.isSuccessful) {
                        Log.e("DashboardFragment", "Gagal mengambil total add monthly: ${responseAddMonthly.code()}")
                    }
                    // Pertimbangkan untuk menampilkan pesan error ke pengguna di sini
                    if (!responseWithdrawMonthly.isSuccessful) {
                        Log.e("DashboardFragment", "Gagal mengambil total add monthly: ${responseWithdrawMonthly.code()}")
                    }
                    // Pertimbangkan untuk menampilkan pesan error ke pengguna di sini
                }

            } catch (e: Exception) {
                Log.e("DashboardFragment", "Error: ${e.message}")
                // Pertimbangkan untuk menampilkan pesan error ke pengguna di sini
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}