package com.huaranga.dayana.lab13

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.huaranga.dayana.lab13.databinding.ListItemImgBinding
import java.io.File

class GalleryAdapter(private val fileArray: Array<File>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListItemImgBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            Glide.with(binding.root.context)
                .load(file)
                .into(binding.localImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fileArray[position])
    }

    override fun getItemCount(): Int {
        return fileArray.size
    }
}