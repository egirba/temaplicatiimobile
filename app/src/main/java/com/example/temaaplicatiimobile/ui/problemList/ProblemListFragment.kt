package com.example.temaaplicatiimobile.ui.problemList

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.temaaplicatiimobile.CustomAdapter
import com.example.temaaplicatiimobile.R
import com.example.temaaplicatiimobile.data.ProblemaSemnalata
import com.example.temaaplicatiimobile.data.ProblemaView
import com.example.temaaplicatiimobile.databinding.FragmentGalleryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class ProblemListFragment : Fragment() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter
    private val listaProbleme: ArrayList<ProblemaView> = ArrayList();
    private var LOG_TAG = "listaProblemeFragment"
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        // below line is to get our menu item.
        val searchItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        val searchView: SearchView = searchItem.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                if (newText != null) {
                    debug(newText)
                    filter(newText)
                }
                return false
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // getting the recyclerview by its id
        recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)



        extractListaProbleme()


        adapter = CustomAdapter(context);
        adapter.setData(listaProbleme)

        recyclerview.adapter = adapter
    }

    private fun extractListaProbleme() {
        val db = Firebase.firestore;
        db.collection("probleme_semnalate")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var problemaSemnalata = ProblemaSemnalata(
                        UUID.nameUUIDFromBytes(
                            document.data.getValue("uniqueId").toString().toByteArray()
                        ),
                        document.data.getValue("userId") as String,
                        document.data.getValue("imagine") as String,
                        document.data.getValue("descriere") as String
                    )
                    listaProbleme.add(
                        ProblemaView(problemaSemnalata.imagine, problemaSemnalata.descriere)
                    )
                    // debug("${document.id} => ${document.data.get('id')}")
                }
                adapter.filterList(listaProbleme)
            }
            .addOnFailureListener { exception ->
                warn("Error getting documents: ", exception)
            }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<ProblemaView> = ArrayList()

        // running a for loop to compare elements.
        for (item in listaProbleme) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.text.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this.context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist)
            // recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun warn(s: String, e: Exception) {
        Log.w(LOG_TAG, s, e)
    }

    fun debug(message: String) {
        Log.d(LOG_TAG, message)
    }
}