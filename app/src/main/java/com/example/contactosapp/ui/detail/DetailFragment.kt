package com.example.contactosapp.ui.detail

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.contactosapp.R
import com.example.contactosapp.data.server.ImageRepository
import com.example.contactosapp.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(safeArgs.contact),
        ImageRepository(requireActivity().application)
        )
    }

    private val PERMISSION_REQUEST_CALL_PHONE = 1
    private val PERMISSION_REQUEST_WRITE_STORAGE = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)

        binding.contactDetailToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            binding.contact = uiState.contact
        }

        binding.contactDetailPhone.setOnClickListener {
            makePhoneCall()
        }

        binding.contactDetailCollapsingImage.setOnClickListener {
            saveImageGallery(binding)
        }


        binding.btnSendEmail.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.sendEmail(safeArgs.contact, context!!)
            }
        }
    }

    private fun saveImageGallery(binding: FragmentDetailBinding) {
        viewModel.hasWritePermission.observe(viewLifecycleOwner) { hasWritePermission ->
            if (hasWritePermission == true) {
                Log.d("pulsando en image","pulsando en image")
                val drawable = binding.contactDetailImage.drawable
                if (drawable is BitmapDrawable) {
                    val bitmap = drawable.bitmap
                    viewModel.saveImageToGallery(bitmap).also {
                        Toast.makeText(context, "Image saved to gallery.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                requestPermission(WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_WRITE_STORAGE)
            }
        }
    }


    private fun makePhoneCall() {
        viewModel.hasPhoneCallPermission.observe(viewLifecycleOwner) { hasPhoneCallPermission ->
            if (hasPhoneCallPermission == true) {
                viewModel.getPhoneCallIntent(safeArgs.contact.phone).also { intent ->
                    startActivity(intent)
                }
            } else {
                requestPermission(CALL_PHONE, PERMISSION_REQUEST_CALL_PHONE)
            }

        }
    }

    private fun requestPermission(permission: String, permissionRequest: Int) {
            requestPermissions(arrayOf(permission), permissionRequest)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.checkPhoneCallPermission(requireContext())
                } else {
                    requireActivity().finish()
                }
            }

            PERMISSION_REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.checkWritePermission(requireContext())
                } else {
                    requireActivity().finish()
                }
            }
        }
    }




}