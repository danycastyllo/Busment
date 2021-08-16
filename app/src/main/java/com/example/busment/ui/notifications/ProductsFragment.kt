package com.example.busment.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.busment.databinding.FragmentProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment() {

    private lateinit var productsViewModel: ProductsViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ProductsViewModel::class.java
            )

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val lista: ListView = binding.lista
        val button: FloatingActionButton = binding.floatingActionButton
//        var listaProductos = listOf<Producto>()
//        notificationsViewModel.listaProductos.observe(viewLifecycleOwner, Observer {
//            listaProductos = it
//        })
//
//        val adapter = ProductosAdapter(lista.context, productsViewModel.listaProductos)
//
//        lista.adapter = adapter


        var listaProductos = emptyList<Producto>()

        val database = AppDatabase.getDatabase(lista.context)

        database.productos().getAll().observe(viewLifecycleOwner, Observer {
            listaProductos = it!! // posiblemente la lista no se muestre por esto (recomendacion es volver a copiar el codigo y intentar resolverlo)

            val adapter = ProductosAdapter(lista.context, listaProductos)

            lista.adapter = adapter
        })

        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(lista.context, ProductoActivity::class.java)
            intent.putExtra("id", listaProductos[position].idProducto)
            startActivity(intent)
        }

        button.setOnClickListener {
            val intent = Intent(lista.context, NuevoProductoActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}