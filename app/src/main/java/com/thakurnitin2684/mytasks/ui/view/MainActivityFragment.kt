package com.thakurnitin2684.mytasks.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thakurnitin2684.mytasks.R
import com.thakurnitin2684.mytasks.data.db.entity.Task
import com.thakurnitin2684.mytasks.databinding.FragmentMainBinding
import com.thakurnitin2684.mytasks.ui.adapter.TaskAdapter
import com.thakurnitin2684.mytasks.ui.viewmodel.TasksViewModel
import com.thakurnitin2684.mytasks.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val TAG = "MainActivityFragment"


@AndroidEntryPoint
class MainActivityFragment : Fragment(), TaskAdapter.OnTaskClickListener {


    private val tasksViewModel : TasksViewModel by activityViewModels()


    lateinit var taskAdapter: TaskAdapter

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    interface OnTaskEdit {
        fun onTaskEdit(task: Task)
        fun onRecyclerViewScroll(y : Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.taskList.layoutManager = LinearLayoutManager(activity)
        taskAdapter = TaskAdapter(this,arrayListOf(), requireActivity())
        binding.taskList.addItemDecoration(
            DividerItemDecoration(
                binding.taskList.context,
                (binding.taskList.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.taskList.adapter = taskAdapter

        tasksViewModel.fetchAllTasks()

        observeNotes()

        binding.taskList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                (activity as OnTaskEdit?)?.onRecyclerViewScroll(dy)
            }
        })

        return view
    }



    private fun observeNotes() {
        tasksViewModel.getAllTasks().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { tasks -> renderTasks(tasks) }
                    Log.d(TAG,"Success")

                }
                Status.LOADING -> {
                    Log.d(TAG,"Loading")
                    Toast.makeText(
                        requireActivity(), "Loading ....",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
        private fun renderTasks(tasks: List<Task>) {
            if(tasks.isEmpty()){
                    binding.noTasksMessage.visibility=View.VISIBLE
                    binding.addTasksMessage.visibility=View.VISIBLE
            }else{
                binding.noTasksMessage.visibility=View.GONE
                binding.addTasksMessage.visibility=View.GONE
            }
            taskAdapter.addData(tasks)
            taskAdapter.notifyDataSetChanged();

        }



    override fun onEditClick(task: Task) {
        (activity as OnTaskEdit?)?.onTaskEdit(task)
    }

    override fun onDeleteClick(task: Task) {
        GlobalScope.launch {
            tasksViewModel.deleteTask(task)
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.main_fragment,MainActivityFragment()).addToBackStack(null).commit()
        }


    }

    override fun onTaskClick(task: Task) {
        (activity as OnTaskEdit?)?.onTaskEdit(task)
    }

}


