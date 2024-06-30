package com.rose.greeneye.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rose.greeneye.data.remote.response.DataPlantResponse
import com.rose.greeneye.databinding.LayoutItemHorizontalBinding
import com.rose.greeneye.databinding.LayoutItemVerticalBinding

class ListItemAdapter(
    private val type: String = HORIZONTAL_TYPE,
    private val onItemClickListener: OnItemClickListener,
) :
    ListAdapter<DataPlantResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onListItemClick(
            position: Int,
            item: DataPlantResponse,
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
            item: DataPlantResponse,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUrl[3])
                    .into(binding.ivPlant)

                tvPlantName.text = item.idName
                tvScientificName.text = item.scienceName
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
            item: DataPlantResponse,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUrl[2])
                    .into(binding.ivPlant)

                tvPlantName.text = item.idName
                tvScientificName.text = item.scienceName
            }
            binding.root.setOnClickListener {
                onItemClickListener.onListItemClick(position, item)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<DataPlantResponse>() {
                override fun areItemsTheSame(
                    oldItem: DataPlantResponse,
                    newItem: DataPlantResponse,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: DataPlantResponse,
                    newItem: DataPlantResponse,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        const val HORIZONTAL_TYPE = "horizontal"
        const val VERTICAL_TYPE = "vertical"
    }
}
