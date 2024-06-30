package com.rose.greeneye.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.databinding.LayoutItemHorizontalBinding

class HistoryItemAdapter(
    private val onItemClickListener: OnItemClickListener,
) : ListAdapter<HistoryEntity, HistoryItemAdapter.ViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onListItemClick(
            position: Int,
            item: HistoryEntity,
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            LayoutItemHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(position, item)
    }

    inner class ViewHolder(private val binding: LayoutItemHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            item: HistoryEntity,
        ) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.imageUrl)
                    .into(ivPlant)

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
            object : DiffUtil.ItemCallback<HistoryEntity>() {
                override fun areItemsTheSame(
                    oldItem: HistoryEntity,
                    newItem: HistoryEntity,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: HistoryEntity,
                    newItem: HistoryEntity,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
