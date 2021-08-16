package com.example.busment.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.busment.R

class ProductsViewModel : ViewModel() {

    val producto = Producto("Camara", 100.0, "Camara ultimo modelo", R.drawable.ic_notifications_black_24dp)
    val producto2 = Producto("PC", 1000.0, "16 GB RAM", R.drawable.ic_home_black_24dp)

    val listaProductos = listOf(producto, producto2)
}