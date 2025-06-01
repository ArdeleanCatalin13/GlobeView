package com.example.globeview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.globeview.R
import com.example.globeview.models.Data

class CountriesAdapter: RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    lateinit var countryFlag: ImageView
    lateinit var countryName: TextView
    lateinit var countryRegion: TextView

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.iso2 == newItem.iso2
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_countries, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = differ.currentList[position]

        countryFlag = holder.itemView.findViewById(R.id.countryFlag)
        countryName = holder.itemView.findViewById(R.id.countryName)
        countryRegion = holder.itemView.findViewById(R.id.countryRegion)
        holder.itemView.apply {
            Glide.with(this).load(country.href.flag).into(countryFlag)
            countryName.text = country.name
            countryRegion.text = country.capital

            setOnClickListener {
                onItemClickListener?.let { it(country) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener

    }
}