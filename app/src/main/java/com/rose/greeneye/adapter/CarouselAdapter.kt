package com.rose.greeneye.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rose.greeneye.data.remote.response.DataPlantResponse
import com.rose.greeneye.databinding.LayoutCarouselItemBinding

class CarouselAdapter(
    private val list: List<DataPlantResponse>,
    private val context: Context,
    private val onCarouselClickListener: OnCarouselClickListener,
) : RecyclerView.Adapter<CarouselAdapter.ItemViewHolder>() {
    interface OnCarouselClickListener {
        fun onCarouselClick(
            position: Int,
            item: DataPlantResponse,
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemViewHolder {
        return ItemViewHolder(
            LayoutCarouselItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
    ) {
        val item = list[position]
        holder.bind(position, item)
    }

    override fun getItemCount() = list.size

    inner class ItemViewHolder(private val binding: LayoutCarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            item: DataPlantResponse,
        ) {
            binding.apply {
                Glide.with(root.context)
                    .load(item.imageUrl[2])
                    .into(ivPlantImage)

                tvPlantName.text = item.idName
                tvScientificName.text = item.scienceName

                root.setOnClickListener {
                    onCarouselClickListener.onCarouselClick(position, item)
                }
            }
        }
    }
}
