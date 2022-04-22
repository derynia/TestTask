package com.android.testtask.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.testtask.AddEditActivity
import com.android.testtask.R
import com.android.testtask.adapters.StationsAdapter
import com.android.testtask.databinding.FragmentStationsBinding
import com.android.testtask.db.entity.Stations
import com.android.testtask.viewmodel.StationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationsFragment : Fragment(R.layout.fragment_stations) {
    private var _binding: FragmentStationsBinding? = null
    private val binding: FragmentStationsBinding get() = _binding!!
    private val stationsViewModel : StationsViewModel by viewModels()
    private val stationsAdapter = StationsAdapter(
        { station -> editStation(station) },
        { station -> deleteStation(station) }
    )

    private fun editStation(station: Stations) {
        openAddEdit(station.id)
    }

    private fun deleteStation(station: Stations) {
        stationsViewModel.delete(station)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        stationsViewModel.getStations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun openAddEdit(id: Long) {
        val addEditIntent = Intent(context, AddEditActivity::class.java)
        addEditIntent.putExtra("ID", id)
        context?.startActivity(addEditIntent)
    }

    private fun setupViews() {
        binding.buttonAdd.setOnClickListener {
            openAddEdit(-1)
        }

        binding.recyclerStations.adapter = stationsAdapter
        val stationsObserver = Observer<List<Stations>> { newStation ->
            stationsAdapter.setList(newStation)
        }

        stationsViewModel.stations.observe(viewLifecycleOwner, stationsObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}