package com.thakurnitin2684.mytasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
private const val TAG="MainActivityFragment"
class MainActivityFragment : Fragment(), MyRecyclerViewAdapter.OnTaskClickListener {
//    private val viewModel : MyTasksViewModel by activityViewModels()
private val viewModel by lazy { ViewModelProvider(requireActivity()).get(MyTasksViewModel::class.java) }
    private val mAdapter = MyRecyclerViewAdapter(null,this)
    interface OnTaskEdit {
        fun onTaskEdit(task: Task)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.cursor.observe(this, Observer { cursor -> mAdapter.swapCursor(cursor)?.close() })
        viewModel.cursor.observe(this, Observer{ cursor ->mAdapter.swapCursor(cursor)?.close()})
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task_list.layoutManager=LinearLayoutManager(context)
        task_list.adapter=mAdapter
    }

    override fun onEditClick(task: Task) {
        (activity as OnTaskEdit?)?.onTaskEdit(task)
    }

    override fun onDeleteClick(task: Task) {
viewModel.deleteTask(task.id)    }

    override fun onTaskClick(task: Task) {
        Toast.makeText(requireActivity()," ${task.name}",Toast.LENGTH_SHORT).show()
        (activity as OnTaskEdit?)?.onTaskEdit(task)
    }

}


