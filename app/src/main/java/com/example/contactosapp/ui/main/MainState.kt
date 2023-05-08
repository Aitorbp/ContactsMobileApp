package com.example.contactosapp.ui.main

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.contactosapp.data.server.Contact


fun Fragment.buildMainState(
    navController: NavController = findNavController()
) = MainState(navController)

class MainState(
    private val navController: NavController
) {
    fun onPokemonClicked(contact: Contact) {
        //navController.navigate(R.id.action_main_to_detail)
        val action = MainFragmentDirections.actionMainToDetail(contact)
        navController.navigate(action)
    }
}