package com.rose.greeneye.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rose.greeneye.data.local.PlantModel
import com.rose.greeneye.databinding.LayoutCarouselItemBinding

class CarouselAdapter(
    private val list: ArrayList<PlantModel>,
    private val context: Context,
    private val onCarouselClickListener: OnCarouselClickListener,
) : RecyclerView.Adapter<CarouselAdapter.ItemViewHolder>() {
    interface OnCarouselClickListener {
        fun onCarouselClick(
            position: Int,
            item: PlantModel,
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
            item: PlantModel,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUri)
                    .into(binding.ivPlantImage)

                tvPlantName.text = item.plant_name
                tvScientificName.text = item.scientific_name

                binding.root.setOnClickListener {
                    onCarouselClickListener.onCarouselClick(position, item)
                }
            }
        }
    }
}
