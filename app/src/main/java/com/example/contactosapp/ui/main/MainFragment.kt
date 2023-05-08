package com.example.contactosapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactosapp.R
import com.example.contactosapp.data.server.ApiResponseStatus
import com.example.contactosapp.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {


    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainState: MainState

    private val adapter = ContactsAdapter { mainState.onPokemonClicked(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainState = buildMainState()


        val binding = FragmentMainBinding.bind(view).apply {

            viewModel.contactsList.observe(viewLifecycleOwner) { contactsList ->
                adapter.submitList(contactsList)
            }

            recycler.adapter = adapter
            recycler.layoutManager as LinearLayoutManager

        }
        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    binding.loadingProgressbar.visibility = View.GONE    // Ocultar progressbar
                    Toast.makeText(context, status.menssageId, Toast.LENGTH_SHORT)
                        .show()
                }
                is ApiResponseStatus.Loaling -> binding.loadingProgressbar.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> binding.loadingProgressbar.visibility =
                    View.GONE // Ocultar progressbar
            }
        }
    }
}