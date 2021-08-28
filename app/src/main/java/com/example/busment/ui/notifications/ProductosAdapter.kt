package com.example.busment.ui.notifications

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.busment.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.graphics.BitmapFactory




class ProductosAdapter(private val mContext: Context, private val listaProductos: List<Producto>) : RecyclerView.Adapter<ProductosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.bind(producto, mContext)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductoActivity::class.java)
            intent.putExtra("id", listaProductos[position].idProducto)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val nombre = view.findViewById<TextView>(R.id.nombre)
        private val precio = view.findViewById<TextView>(R.id.precio)
        private val imageView = view.findViewById<ImageView>(R.id.imageView)

        fun bind(producto: Producto, mContext: Context) {
            val imageUri = ImageController.getImageUri(mContext, producto.idProducto.toLong())
//            val imageBitmap = ImageController.uriToBitmap(mContext, imageUri)

            nombre.text = producto.nombre
            precio.text = "$${producto.precio}"
            imageView.setImageURI(imageUri)
        }

    }


}