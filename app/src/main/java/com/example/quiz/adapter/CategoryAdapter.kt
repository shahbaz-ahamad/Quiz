package com.example.quiz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.R
import com.example.quiz.databinding.EachListBinding


class CategoryAdapter(val categoryList: ArrayList<String>, val categoryImage: ArrayList<Int>) : RecyclerView.Adapter<CategoryAdapter.viewholder>() {
    class viewholder(val binding:EachListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String, categoryImage: Int) {
            binding.apply {
                quizTitleList.text=category
                quizImageList.setImageResource(categoryImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        return viewholder(EachListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val category = categoryList[position]
        val categoryImage =categoryImage[position]
        holder.bind(category,categoryImage)
        holder.itemView.setOnClickListener{
            onClick?.invoke(category,categoryImage)
        }
    }

    var onClick :((String,Int)-> Unit)?= null
}