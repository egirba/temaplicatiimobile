package com.example.temaaplicatiimobile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.temaaplicatiimobile.data.ProblemaView
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(private var context: Context?) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var listaProbleme: ArrayList<ProblemaView> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = listaProbleme[position]
        val imageBytes = Base64.decode(ItemsViewModel.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageBitmap(image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text
        holder.shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, ItemsViewModel.text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context?.startActivity(shareIntent)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listaProbleme.size
    }

    fun setData(filterList: ArrayList<ProblemaView>) {
        listaProbleme = filterList;
        notifyDataSetChanged();
    }

    fun filterList(filterList: ArrayList<ProblemaView>) {
        listaProbleme = filterList;
        notifyDataSetChanged();
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val shareButton: MaterialButton = itemView.findViewById(R.id.shareButton)
    }


}
