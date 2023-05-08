package com.example.contactosapp.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactosapp.data.server.Contact


@BindingAdapter("items")
fun RecyclerView.setItems(contacts: List<Contact>?) {
    if (contacts != null) {
        (adapter as? ContactsAdapter)?.submitList(contacts)
    }
}