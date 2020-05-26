package com.thakurnitin2684.mytasks

import android.R.attr.phoneNumber
import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.android.synthetic.main.edit_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*


private const val TAG="AddEditFragment"
private const val ARG_TASK = "task"
var time = ""
var time2=""
const val channelID = "channelID"
const val channelName = "Channel Name"

class AddEditFragment : Fragment() {
    private var timeBool = false
    private var notificationTime :Long=0
//    private var taskName = "MyTasks"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.edit_fragment, container, false)
        val viewModel : MyTasksViewModel by activityViewModels()
        createNC()
        Log.d(TAG,"onCreateView started")
        var tID =0
        if(arguments != null) {
            val strtTask = requireArguments().getParcelable<Task>("prevTask")
            if (strtTask != null) {
                root.edit_Title.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.name)
                root.edit_Description.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.description)
                time=strtTask.time
               tID=strtTask.id.toInt()
               root.edit_show_time.text=time

            }
        }

        root.edit_time.setOnClickListener {
            showDateTimeDialog(edit_show_time)
            timeBool = true
        }
        root.edit_save.setOnClickListener{
            val title = root.edit_Title.editText?.text.toString()
            val description = root.edit_Description.editText?.text.toString()
//            taskName=title
            val task = Task(
                title,description ,if(timeBool) time2 else time ,tID.toLong()
            )
               viewModel.saveTask(task)
            activity?.onBackPressed()

            val intent = Intent(requireActivity(),ReminderBroadcast::class.java)
            intent.putExtra(ReminderBroadcast.TASK_NAME,title)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            val timeAtBC = System.currentTimeMillis()
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,notificationTime,pendingIntent)
            }
        }

        return root
    }
//    fun getTaskName(): String? {
//        return taskName
//    }
    private fun createNC(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name:CharSequence = "mychannel"
            var description = "description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                "myChannelId",
                name,
                importance
            )
            notificationChannel.description=description
//            val notificationManager =
//                this.ge(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }

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
                        time2=simpleDateFormat.format(calendar.time)
                        notificationTime=calendar.time.time
                        date_time_in.text= time2
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


}
