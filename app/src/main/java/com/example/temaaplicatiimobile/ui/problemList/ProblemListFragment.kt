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

    private val binding get() = _binding!!

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        //Setare actiuni declansate la search
        //De fiecare data cand tastam ceva in casuta de search
        //se va declansa onQueryTextChange
        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    //Se va executa filtrarea folosind textul din casuta de search
                    filter(newText)
                }
                return false
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Setare RecyclerView
        // - se identifica dupa ID
        // - se construieste adapter-ul
        // - se interogheaza baza de date in extractListaProbleme
        // - se adauga adapter-ul la recyclerview
        recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        extractListaProbleme()
        adapter = CustomAdapter(context);
        adapter.setData(listaProbleme)
        recyclerview.adapter = adapter
    }

    private fun extractListaProbleme() {
        //interogare baza de date
        //-- se iau toate rezultatele din colectia "probleme_semnalate"
        //-- pentru fiecare document, se construieste un obiect ProblemaSemnalata
        //-- se adauga lista in adapter, care o sa faca refresh la RecyclerView
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
        // construire lista goala
        val filteredlist: ArrayList<ProblemaView> = ArrayList()

        // parcurgere fiecare element din lista originala de probleme
        for (item in listaProbleme) {
            //verificat daca element-ul curent contine text-ul cautat
            if (item.text.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            //daca nu avem rezultate, afisam un mesaj
            Toast.makeText(this.context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // trimitem lista rezultata catre adapter
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

    private fun debug(message: String) {
        Log.d(LOG_TAG, message)
    }

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
}