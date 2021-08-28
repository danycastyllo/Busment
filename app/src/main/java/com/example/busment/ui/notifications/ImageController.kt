package com.example.busment.ui.notifications

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.Uri.parse
import android.provider.MediaStore
import java.io.*

object ImageController {
    fun selectPhotoFromGallery(activity: Activity, code: Int) { // Seleccionar imagen de la galeria
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, id: Long, uri: Uri) {

        val imagen = uriToBitmap(context, uri)
        val bytes = getImageUri(imagen)

        val file = File(context.filesDir, id.toString())

//        val bytes = context.contentResolver.openInputStream(urii)?.readBytes()!!
        file.writeBytes(bytes)
    }
    fun uriToBitmap(context: Context, uri: Uri):Bitmap { // convertir uri a bitmap
        val imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
        val ancho = imageBitmap.width / 10
        val largo = imageBitmap.height / 10
        val imagen = Bitmap.createScaledBitmap(imageBitmap, ancho, largo, true)
        return imagen
    }

    fun getImageUri(bitmap: Bitmap): ByteArray { // Obtener un ByteArray de un bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return image
    }



    fun getImageUri(context: Context, id: Long): Uri { // devuelve la imagen en un Uri
        val file = File(context.filesDir, id.toString())

        return if (file.exists()) Uri.fromFile(file)
        else parse("android.resource://com.example.busment/drawable/placeholder")
    }

    fun deleteImage(context: Context, id: Long) {
        val file = File(context.filesDir, id.toString())
        file.delete()
    }
}