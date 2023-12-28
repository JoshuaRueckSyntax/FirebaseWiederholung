package com.example.firebasewiederholung.adapter

import android.service.autofill.Dataset
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasewiederholung.FirebaseViewModel
import com.example.firebasewiederholung.R
import com.example.firebasewiederholung.databinding.ItemTodoBinding
import com.example.firebasewiederholung.model.TodoItem

class TodoAdapter(
    private val dataset: List<TodoItem>,
    private val viewModel: FirebaseViewModel
): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = dataset[position]

        if(item.checked){
            holder.binding.tvIndex.isVisible = false
            holder.binding.tvTitleTodo.text = item.text
            holder.binding.check.isChecked = true
            holder.binding.cvTodo.setCardBackgroundColor(getColor(holder.itemView.context, R.color.yellow_light_primary))
            holder.binding.check.setOnClickListener {
                item.checked = false
                viewModel.saveTodo(item)
                viewModel.deleteTodo(item)
            }
        } else {
            holder.binding.tvIndex.text = "#" + (position+1)
            holder.binding.tvTitleTodo.text = item.text
            holder.binding.check.setOnClickListener {
                item.checked = true
                viewModel.saveTodo(item)
                viewModel.deleteTodo(item)
            }
        }

    }

}