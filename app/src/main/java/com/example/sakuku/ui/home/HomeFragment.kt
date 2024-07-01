package com.example.sakuku.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sakuku.R
import com.example.sakuku.databinding.FragmentHomeBinding
import com.example.sakuku.network.ApiConfig
import com.example.sakuku.model.Transaction
import com.example.sakuku.network.ApiService
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Fungsi ini harus mengembalikan objek View
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi ApiService
        apiService = ApiConfig().getApiService()

        // Setup Spinner
        val spinnerCategory: Spinner = binding.cardTotalUangKas.spinnerCategory
        val categories = resources.getStringArray(R.array.categories)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Handle tombol submit
        binding.cardTotalUangKas.btnSubmit.setOnClickListener {
            val name = binding.cardTotalUangKas.etName.text.toString()
            val description = binding.cardTotalUangKas.etDescription.text.toString()
            val category = spinnerCategory.selectedItem.toString()
            val amountString = binding.cardTotalUangKas.etAmount.text.toString()
            val date = binding.cardTotalUangKas.etDate.text.toString()

            // Validasi input (pastikan semua field terisi, format tanggal benar, dll.)

            val amount = amountString.toIntOrNull() ?: 0 // Handle jika amount bukan angka

            val transaction = Transaction(
                category = category,
                description = description,
                date_update = date,
                user = name,
                add_transaction = amount,
                withdraw_transaction = 0 // Atau ambil nilai dari field lain jika ada
            )

            lifecycleScope.launch {
                try {
                    val response = apiService.addTransaction(transaction)
                    if (response.isSuccessful) {
                        // Berhasil mengirim data
                        Toast.makeText(
                            requireContext(),
                            "Transaksi berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Reset formulir atau lakukan tindakan lain yang sesuai
                    } else {
                        // Tangani kesalahan
                        Log.e("HomeFragment", "Gagal menambahkan transaksi: ${response.code()}")
                        Toast.makeText(
                            requireContext(),
                            "Gagal menambahkan transaksi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    // Tangani pengecualian
                    Log.e("HomeFragment", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}