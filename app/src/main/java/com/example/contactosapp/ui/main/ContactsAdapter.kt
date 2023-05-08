package com.example.contactosapp.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactosapp.R
import com.example.contactosapp.data.server.Contact
import com.example.contactosapp.databinding.ViewContactBinding
import com.example.contactosapp.ui.common.basicDiffUtil
import com.example.contactosapp.ui.common.inflate


class ContactsAdapter (private val listener: (Contact) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ViewHolder>(basicDiffUtil { old, new -> old.name == new.name }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_contact, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
        holder.itemView.setOnClickListener { listener(contact) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewContactBinding.bind(view)
        fun bind(contact: Contact) {
            binding.contact = contact
        }
    }
}