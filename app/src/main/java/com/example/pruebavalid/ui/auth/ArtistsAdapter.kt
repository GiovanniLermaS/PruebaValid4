package com.example.pruebavalid.ui.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavalid.R
import com.example.pruebavalid.data.db.entities.Artist
import com.squareup.picasso.Picasso


class ArtistsAdapter(private val context: Context, private var arrayArtist: ArrayList<Artist>,
                     private var temporalArrayArtist: ArrayList<Artist>) :
    RecyclerView.Adapter<ArtistsAdapter.MyViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = arrayArtist[position].name
        Picasso.with(context).load(arrayArtist[position].url).into(holder.thumbnail)
    }

    override fun getItemCount() = arrayArtist.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                arrayArtist = if (charString.isNotEmpty()) {
                    val filteredList = ArrayList<Artist>()
                    for (row in arrayArtist)
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!!)
                            filteredList.add(row)
                    filteredList
                } else temporalArrayArtist

                val filterResults = FilterResults()
                filterResults.values = arrayArtist
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayArtist = results?.values as ArrayList<Artist>
                notifyDataSetChanged()
            }
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)
        val title = view.findViewById<TextView>(R.id.title)
    }
}