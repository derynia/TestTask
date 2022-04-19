package com.android.testtask.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.testtask.AddEditActivity
import com.android.testtask.R
import com.android.testtask.databinding.FragmentStationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationsFragment : Fragment(R.layout.fragment_stations) {
    private var _binding: FragmentStationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStationsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAdd.setOnClickListener {
            val addEditIntent = Intent(context, AddEditActivity::class.java)
            addEditIntent.putExtra("ID", 0)
            context?.startActivity(addEditIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}