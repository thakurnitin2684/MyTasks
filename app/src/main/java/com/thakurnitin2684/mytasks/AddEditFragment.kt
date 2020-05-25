package com.thakurnitin2684.mytasks

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.android.synthetic.main.edit_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*


private const val TAG="AddEditFragment"
private const val ARG_TASK = "task"
var time = ""
const val channelID = "channelID"
const val channelName = "Channel Name"

class AddEditFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.edit_fragment, container, false)
        val viewModel : MyTasksViewModel by activityViewModels()
        Log.d(TAG,"onCreateView started")
        var tID:Long=0
        if(arguments != null) {
            val strtTask = requireArguments().getParcelable<Task>("prevTask")
            if (strtTask != null) {
                root.edit_Title.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.name)
                root.edit_Description.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.description)
                time=strtTask.time
               tID=strtTask.id
               root.edit_show_time.text=time

            }
        }

        root.edit_time.setOnClickListener{
            showDateTimeDialog(edit_show_time)

        }
        root.edit_save.setOnClickListener{
            val title = root.edit_Title.editText?.text.toString()
            val description = root.edit_Description.editText?.text.toString()

            val task = Task(
                title,description , time,tID
            )
               viewModel.saveTask(task)
            activity?.onBackPressed()
        }
        return root
    }
    private fun showDateTimeDialog(date_time_in: TextView) {
        val calendar: Calendar = Calendar.getInstance()

        val dateSetListener =
            OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val timeSetListener =
                    OnTimeSetListener { view, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        val simpleDateFormat = SimpleDateFormat("EEE, d MMM  HH:mm")
                        time=simpleDateFormat.format(calendar.time)
                        date_time_in.text= time
                    }
                TimePickerDialog(
                    activity,
                    timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }
        DatePickerDialog(
            requireActivity(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()

    }
//   fun NotificationBuild(){
//       var builder = NotificationCompat.Builder(requireContext(), channelID)
//           .setSmallIcon(R.drawable.ic_launcher_foreground)
//           .setContentTitle("textTitle")
//           .setContentText("textContent")
//           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//       val notificationManager = NotificationManager
//   }

}
