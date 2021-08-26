package com.example.busment.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.busment.MainActivity
import com.example.busment.R
import com.example.busment.databinding.FragmentProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment() {

    private lateinit var productsViewModel: ProductsViewModel
    private var _binding: FragmentProductsBinding? = null
    private lateinit var database: AppDatabase

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productsViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ProductsViewModel::class.java
            )

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val lista: RecyclerView = binding.lista
        val button: FloatingActionButton = binding.floatingActionButton
        var listaProductos: List<Producto>

        database = AppDatabase.getDatabase(lista.context)

        database.productos().getAll().observe(viewLifecycleOwner, {
            listaProductos = it

            val adapter = ProductosAdapter(lista.context, listaProductos)

            lista.adapter = adapter
        })

        button.setOnClickListener {
            val intent = Intent(lista.context, NuevoProductoActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setSubmitButtonEnabled(true)
        searchView.setQueryHint("Search Product")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                searchDatabase(query)
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDatabase(query)
                return true
            }
        })
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        database = AppDatabase.getDatabase(lista.context)

        database.productos().searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                val adapter = ProductosAdapter(lista.context, it)
                lista.adapter = adapter
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}