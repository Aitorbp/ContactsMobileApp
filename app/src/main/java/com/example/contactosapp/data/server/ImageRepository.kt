package com.example.contactosapp.data.server

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore

class ImageRepository(private val context: Context) {

    fun saveImageToGallery(bitmap: Bitmap): Result<Unit> {
        try {
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: return Result.Error(Exception("Failed to create new MediaStore record."))

            context.contentResolver.openOutputStream(uri).use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    return Result.Error(Exception("Failed to save bitmap."))
                }
            }

            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}








