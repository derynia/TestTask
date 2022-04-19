package com.android.testtask

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android.testtask.databinding.ActivityAddEditBinding
import com.android.testtask.db.FuelType
import dagger.hilt.android.AndroidEntryPoint


private lateinit var binding: ActivityAddEditBinding
@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.buttonCancel.setOnClickListener {

        }

        binding.spinnerFuelType.adapter = ArrayAdapter<FuelType>(this, R.layout.simple_spinner_item, FuelType.values())
    }
}