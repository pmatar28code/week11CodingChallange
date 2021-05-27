package com.example.week11codingchallange


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.week11codingchallange.databinding.ItemWordBinding


class WordsAdapter: ListAdapter<Words, WordsAdapter.ItemViewHolder>(diff){
    companion object{
        val diff = object : DiffUtil.ItemCallback<Words>(){
            override fun areItemsTheSame(oldItem: Words, newItem: Words): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Words, newItem: Words): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWordBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ItemViewHolder(
        private val binding : ItemWordBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun onBind(item: Words){
            binding.wordTextView.text = item.word
        }
    }
}