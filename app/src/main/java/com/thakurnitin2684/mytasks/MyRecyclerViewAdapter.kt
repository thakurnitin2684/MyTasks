package com.thakurnitin2684.mytasks

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import java.lang.IllegalArgumentException

private const val TAG = "RecyclerViewAdapter"
class TaskViewHolder( override val containerView: View) : RecyclerView.ViewHolder(containerView) ,LayoutContainer{
    var each_title: TextView = containerView.findViewById(R.id.each_name)
    var each_time: TextView = containerView.findViewById(R.id.each_time)
    var each_edit : ImageView = containerView.findViewById(R.id.each_edit)
    var each_delete: ImageView = containerView.findViewById(R.id.each_delete)
    fun bind(task :Task,listener : MyRecyclerViewAdapter.OnTaskClickListener)
    {
        each_title.text = task.name
        each_time.text=task.time
//        tli_delete.visibility=View.VISIBLE
//        tli_edit.visibility=View.VISIBLE

        each_edit.setOnClickListener{
                listener.onEditClick(task)
        }
        each_delete.setOnClickListener{
                  listener.onDeleteClick(task)
        }
        containerView.setOnClickListener{
              listener.onTaskClick(task)
        }
    }
}
class MyRecyclerViewAdapter( private var cursor: Cursor?,private val listener: OnTaskClickListener ) :
    RecyclerView.Adapter<TaskViewHolder>() {
    interface OnTaskClickListener{
        fun onEditClick(task:Task)
        fun onDeleteClick(task:Task)
        fun onTaskClick(task:Task)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.d(TAG, "onCreateViewHolder : new view Requested")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.each_task, parent, false)
        return TaskViewHolder(view)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: starts")
        val cursor = cursor
        if (cursor == null || cursor.count == 0) {
            Log.d(TAG, "onBindViewHolder: providing instructions")
            holder.each_title.setText(R.string.instructions_heading)
            holder.each_time.setText(R.string.instructions)

        } else {
            if (!cursor.moveToPosition(position)) {
                throw IllegalArgumentException("Couldn't move cursor to position $position")
            }
            //create a task object from the data in the cursor
            val task = Task(
                cursor.getString(cursor.getColumnIndex(TasksTable.Columns.TASK_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksTable.Columns.TASK_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(TasksTable.Columns.TASK_TIME))
            )
            //Remember that the id isn't set in the constructor
            task.id = cursor.getLong(cursor.getColumnIndex(TasksTable.Columns.ID))
            holder.bind(task,listener)


        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: starts")
        val cursor = cursor
        val count = if (cursor == null || cursor.count == 0) {
            1//fib, because we populate a single viewHolder with instructions
        } else {
            cursor.count
        }
        Log.d(TAG, "returning $count")
        return count
    }
    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val numItems = itemCount
        val oldCursor = cursor
        cursor = newCursor
        if (newCursor != null) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, numItems)
        }
        return oldCursor
    }
}