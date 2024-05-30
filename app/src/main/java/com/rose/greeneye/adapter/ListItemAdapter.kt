package com.rose.greeneye.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rose.greeneye.data.local.PlantModel
import com.rose.greeneye.databinding.LayoutItemHorizontalBinding
import com.rose.greeneye.databinding.LayoutItemVerticalBinding

class ListItemAdapter(
    private val type: String = HORIZONTAL_TYPE,
    private val onItemClickListener: OnItemClickListener,
) :
    ListAdapter<PlantModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onListItemClick(
            position: Int,
            item: PlantModel,
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (type == HORIZONTAL_TYPE) {
            HorizontalViewHolder(
                LayoutItemHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        } else {
            VerticalViewHolder(
                LayoutItemVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when (holder) {
            is HorizontalViewHolder -> holder.bind(position, item)
            is VerticalViewHolder -> holder.bind(position, item)
        }
    }

    inner class HorizontalViewHolder(private val binding: LayoutItemHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            item: PlantModel,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUri)
                    .into(binding.ivPlant)

                tvPlantName.text = item.plant_name
                tvScientificName.text = item.scientific_name
            }
            binding.root.setOnClickListener {
                onItemClickListener.onListItemClick(position, item)
            }
        }
    }

    inner class VerticalViewHolder(private val binding: LayoutItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            item: PlantModel,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUri)
                    .into(binding.ivPlant)

                tvPlantName.text = item.plant_name
                tvScientificName.text = item.scientific_name
            }
            binding.root.setOnClickListener {
                onItemClickListener.onListItemClick(position, item)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PlantModel>() {
                override fun areItemsTheSame(
                    oldItem: PlantModel,
                    newItem: PlantModel,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: PlantModel,
                    newItem: PlantModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        const val HORIZONTAL_TYPE = "horizontal"
        const val VERTICAL_TYPE = "vertical"
    }
}
