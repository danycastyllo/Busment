package com.example.busment.ui.notifications

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_nuevo_producto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.busment.R


class NuevoProductoActivity : AppCompatActivity() {
    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null
    var items = arrayOf("Bebidas", "Lacteos", "Comida", "Dulces", "Otros")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)

        // Initialization Category Exposed Drop-Down Menu
        val adapterItems = ArrayAdapter(this, R.layout.list_item, items)
        auto_complete_txt.setAdapter(adapterItems)

//        auto_complete_txt.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                val item = parent.getItemAtPosition(position).toString()
//                Toast.makeText(applicationContext, "Item: $item", Toast.LENGTH_SHORT).show()
//            }
        //-----------------------------------------------------------------------------------------------

        var idProducto: Int? = null

        if (intent.hasExtra("producto")) {
            val producto = intent.extras?.getSerializable("producto") as Producto

            nombre_et.setText(producto.nombre)
            precio_et.setText(producto.precio.toString())
            descripcion_et.setText(producto.descripcion)
            auto_complete_txt.setText(producto.category, false)

            idProducto = producto.idProducto

            imageUri = ImageController.getImageUri(this, idProducto.toLong())
            imageSelect_iv.setImageURI(imageUri)
        }

        val database = AppDatabase.getDatabase(this)

        save_btn.setOnClickListener {
            val nombre = nombre_et.text.toString()

            val precio:Double = if (precio_et.text.isNullOrEmpty()) 0.0 else precio_et.text.toString().toDouble()

            val descripcion = descripcion_et.text.toString()
            val category = auto_complete_txt.text.toString()

            val producto = Producto(nombre, precio, descripcion, category, R.drawable.ic_launcher_background)

            if (idProducto != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    producto.idProducto = idProducto

                    database.productos().update(producto)

                    imageUri?.let {
                        val intent = Intent()
                        intent.data = it
                        setResult(Activity.RESULT_OK, intent)
                        ImageController.saveImage(this@NuevoProductoActivity, idProducto.toLong(), it)
                    }

                    this@NuevoProductoActivity.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = database.productos().insertAll(producto)[0]

                    imageUri?.let {
                        val intent = Intent()
                        intent.data = it
                        setResult(Activity.RESULT_OK, intent)
                        ImageController.saveImage(this@NuevoProductoActivity, id, it)
                    }

                    this@NuevoProductoActivity.finish()
                }
            }
        }

        imageSelect_iv.setOnClickListener {
            ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)
        }
        rotate_btn.setOnClickListener{
            val bit = imageUri?.let { it1 -> ImageController.uriToBitmap(this, it1) }
            val rot = bit?.let { it1 -> ImageController.RotateBitmap(it1, 90F) }
            imageUri = rot?.let { it1 -> ImageController.BitmapToUri(this, it1) }
            imageSelect_iv.setImageBitmap(rot)

//            val actualtDBPath = getDatabasePath("app_database.db").absolutePath
//            Toast.makeText(applicationContext, " $actualtDBPath", Toast.LENGTH_SHORT).show()

        }

        if (savedInstanceState != null){ // recupera los datos guardados en onSaveInstanceState
            imageUri = savedInstanceState.getParcelable("uri")
            imageSelect_iv.setImageURI(imageUri)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) { // cuando se gira la pantalla o se reinicia la actividad guarda los datos
        super.onSaveInstanceState(outState)
        outState.putParcelable("uri", imageUri)
//        val pri = imageUri.toString()
//        Toast.makeText(applicationContext, " $pri", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data
                val imagen = imageUri?.let { ImageController.uriToBitmap(this, it) }?.let {
                    ImageController.compressBitmap(this, it)
                }
                imageUri = imagen?.let { ImageController.BitmapToUri(this, it) }
                imageSelect_iv.setImageBitmap(imagen)
            }
        }
    }
}