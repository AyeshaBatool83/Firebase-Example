package com.example.firebase.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebase.R
import com.example.firebase.data.models.RetroPhotoItem
import com.example.firebase.databinding.ItemRowBinding


class CustomAdapter(private val dataList: List<RetroPhotoItem>) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    private lateinit var context:Context
    inner class CustomViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val from = LayoutInflater.from(parent.context)
        val view = ItemRowBinding.inflate(from,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder) {
            with(dataList[position]) {
                binding.titleTv.text = this.title
                Glide.with(context)
                    .load(this.url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imgIv)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size
}