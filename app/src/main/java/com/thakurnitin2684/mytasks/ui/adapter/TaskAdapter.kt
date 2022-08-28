package com.thakurnitin2684.mytasks.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thakurnitin2684.mytasks.data.db.entity.Task
import com.thakurnitin2684.mytasks.databinding.EachTaskBinding


class TaskAdapter(
    private val listener: OnTaskClickListener,
    private val tasks: ArrayList<Task>,
    private val context: Context
) :
    RecyclerView.Adapter<TaskAdapter.DataViewHolder>() {

    private lateinit var binding: EachTaskBinding




    interface OnTaskClickListener {
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
        fun onTaskClick(task: Task)
    }





    class DataViewHolder(private val binding: EachTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task, context: Context, listener: OnTaskClickListener) {

            binding.eachName.text = task.name

            binding.eachTime.text = task.time

            binding.eachDelete.setOnClickListener {
                listener.onDeleteClick(task)
            }
            binding.root.setOnClickListener {
                listener.onTaskClick(task)
            }

        }
    }






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : DataViewHolder {
        binding = EachTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)

    }


    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(tasks[position],context,listener)

    fun addData(list: List<Task>) {
        tasks.clear()

        tasks.addAll(list)

    }

}