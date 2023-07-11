package com.example.busment.ui.notifications

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductosDao {
    @Query("SELECT * FROM productos")
    fun getAll(): LiveData<List<Producto>>

    @Query("SELECT * FROM productos WHERE idProducto = :id")
    fun get(id: Int): LiveData<Producto>

    @Query("SELECT * FROM productos  WHERE nombre LIKE :query")
    fun getProductName(query: String): LiveData<List<Producto>>

    @Insert
    fun insertAll(vararg productos: Producto): List<Long>

    @Query("SELECT * FROM productos WHERE nombre LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Producto>>

    @Update
    fun update(producto: Producto)

    @Delete
    fun delete(producto: Producto)

}