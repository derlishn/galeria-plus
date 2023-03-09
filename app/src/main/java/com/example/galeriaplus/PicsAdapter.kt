package com.example.galeriaplus

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.galeriaplus.databinding.PicRowBinding

class PicsAdapter(
    val context:Context,
    val myList: List<PicModel>
) : RecyclerView.Adapter<PicsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: PicRowBinding): ViewHolder(binding.root) {
        val img=binding.ivImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pic_row, parent, false)
        return MyViewHolder(PicRowBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=myList[position]
        Glide.with(context)
            .load(model.uri)
            .into(holder.img)
    }

}