package com.example.busment.ui.notifications

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
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

//        val imagen = compressBitmap(context, uriToBitmap(context, uri))
//        val bytes = getImageByteArray(imagen)

        val file = File(context.filesDir, id.toString())

        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
    }
    fun uriToBitmap(context: Context, uri: Uri):Bitmap { // convertir uri a bitmap
        val imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
        return imageBitmap
    }
    fun compressBitmap(bitmap: Bitmap):Bitmap{ // reduce el tamaño de la imagen para mayor rendimiento
        val ancho = bitmap.width / 10
        val largo = bitmap.height / 10
        val imagen = Bitmap.createScaledBitmap(bitmap, ancho, largo, true)
        return imagen
    }

    fun BitmapToUri(context: Context, bitmap: Bitmap):Uri{
        val file = File(context.cacheDir,"CUSTOM NAME") //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

        val URI = Uri.fromFile(file)

        return URI
    }

    fun getImageByteArray(bitmap: Bitmap): ByteArray { // Obtener un ByteArray de un bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return image
    }


    fun RotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
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