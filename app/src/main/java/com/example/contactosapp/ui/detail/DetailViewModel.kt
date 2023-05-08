package com.example.contactosapp.ui.detail

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.contactosapp.data.server.Contact
import com.example.contactosapp.data.server.ImageRepository
import com.example.contactosapp.data.server.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class DetailViewModel(contact: Contact, private val imageRepository: ImageRepository) : ViewModel() {

    class UiState(val contact: Contact)



    private val _state = MutableLiveData<UiState>()
    val state: LiveData<UiState> = _state

    private val _hasPhoneCallPermission = MutableLiveData<Boolean>(false)
    val hasPhoneCallPermission: LiveData<Boolean> = _hasPhoneCallPermission

    private val _hasWritePermission = MutableLiveData<Boolean>(false)
    val hasWritePermission: LiveData<Boolean> = _hasWritePermission

    init {
        _state.value = UiState(contact)
    }

    fun getPhoneCallIntent(phoneNumber: String): Intent {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        return callIntent
    }

    fun checkPhoneCallPermission(context: Context) {
        val hasPermission = ContextCompat.checkSelfPermission(context,  CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        _hasPhoneCallPermission.postValue(hasPermission)
    }

    fun checkWritePermission(context: Context) {
        val hasPermission = ContextCompat.checkSelfPermission(context,  WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        _hasWritePermission.postValue(hasPermission)
    }


    fun saveImageToGallery(bitmap: Bitmap): Result<Unit> {
        return imageRepository.saveImageToGallery(bitmap)
    }
/*
    fun sendcEmail(contact: Contact, context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.email))
            putExtra(Intent.EXTRA_STREAM, getPhotoUri(context, contact.picture.medium))
        }

        context.startActivity(Intent.createChooser(intent, "Enviar correo electrónico"))
    }
 */
    fun sendEmail(contact: Contact, context: Context) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val photoUri = getPhotoUri(context, contact.picture.medium)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.email))
                putExtra(Intent.EXTRA_STREAM, photoUri)
            }
            context.startActivity(Intent.createChooser(intent, "Enviar correo electrónico"))
        }
    }


    private suspend fun getPhotoUri(context: Context, imageUrl: String): Uri? {
        return withContext(Dispatchers.IO) { // mover la operación de red a un hilo secundario
            val imageFile = File(context.cacheDir, "contact_photo.jpg")
            val inputStream = URL(imageUrl).openStream()
            FileOutputStream(imageFile).use { output ->
                inputStream.copyTo(output)
            }
            return@withContext FileProvider.getUriForFile(
                context,
                "com.example.app.fileprovider",
                imageFile
            )
        }
    }

    /*
   private fun getPhotoUri(contact: Contact, context: Context): Uri? {
        val photoBitmap = BitmapFactory.decodeResource(context.resources, contact.picture.medium)
        val imageFile = File(context.cacheDir, "contact_photo.jpg")
        FileOutputStream(imageFile).use {
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return FileProvider.getUriForFile(context, "com.example.app.fileprovider", imageFile)
    }
*/
    private fun getPhotoxUri(context: Context, imageUrl: String): Uri? {
        val imageFile = File(context.cacheDir, "contact_photo.jpg")
        val inputStream = URL(imageUrl).openStream()
        FileOutputStream(imageFile).use { output ->
            inputStream.copyTo(output)
        }
        return FileProvider.getUriForFile(context, "com.example.app.fileprovider", imageFile)
    }


}





@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val contact: Contact, private val imageRepository: ImageRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(contact, imageRepository) as T
    }
}