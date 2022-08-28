package com.thakurnitin2684.mytasks.ui.view

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.thakurnitin2684.mytasks.R
import com.thakurnitin2684.mytasks.ReminderBroadcast
import com.thakurnitin2684.mytasks.ReminderBroadcast.Companion.TASK_DES
import com.thakurnitin2684.mytasks.ReminderBroadcast.Companion.TASK_NAME
import com.thakurnitin2684.mytasks.data.db.entity.Task
import com.thakurnitin2684.mytasks.databinding.EditFragmentBinding
import com.thakurnitin2684.mytasks.ui.view.MainActivity.Companion.TASK_DATA
import com.thakurnitin2684.mytasks.ui.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "AddEditFragment"

@AndroidEntryPoint
class AddEditFragment : Fragment() {
    private var previousTime = ""
    private var selectedTime = ""
    private var timeBool = false
    private var notificationTime: Long = 0


    private var _binding: EditFragmentBinding? = null
    private val binding get() = _binding!!


    private val tasksViewModel: TasksViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EditFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        createNC()

        var taskID = 0

        //if it is an old task
        if (arguments != null) {
            val strtTask = requireArguments().getParcelable<Task>(TASK_DATA)
            if (strtTask != null) {
                binding.editTitle.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.name)

                binding.editDescription.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.description)

                previousTime = strtTask.time
                taskID = strtTask.id.toInt()
                binding.editShowTime.text = previousTime
            }
        }

        binding.editTime.setOnClickListener {
            showDateTimeDialog(binding.editShowTime)
            timeBool = true
        }
        binding.editSave.setOnClickListener {
            val title = binding.editTitle.editText?.text.toString()
            if (title.isEmpty()) {
                binding.messageTextView.visibility=View.VISIBLE
            } else {
                val description = binding.editDescription.editText?.text.toString()
                val task = Task(
                    id = taskID.toLong(),
                    name = title,
                    description = description,
                    time = if (timeBool) selectedTime else previousTime,
                )

                GlobalScope.launch {
                    if (taskID != 0) { //for update note
                        tasksViewModel.updateTask(task)
                        Log.d(TAG, "updated")
                    } else { //for insert note
                        tasksViewModel.insertTask(task)
                        Log.d(TAG, "insert: ${task.time}")
                    }
                }

                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    activity?.onBackPressed()


                } else {
                    Toast.makeText(context, "Your Task is saved", Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragment,MainActivityFragment()).addToBackStack(null).commit()
                }

                if (timeBool) {
                    val intent = Intent(requireActivity(), ReminderBroadcast::class.java)
                    intent.putExtra(TASK_NAME, title)
                    intent.putExtra(TASK_DES, description)


                    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getBroadcast(
                            context, getCurrentRequestCode(
                                context?.applicationContext
                            ), intent, PendingIntent.FLAG_MUTABLE
                        )
                    } else {
                        PendingIntent.getBroadcast(
                            context, getCurrentRequestCode(
                                context?.applicationContext
                            ), intent, PendingIntent.FLAG_ONE_SHOT
                        )
                    }


                    (requireActivity().getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.set(
                        AlarmManager.RTC_WAKEUP,
                        notificationTime,
                        pendingIntent
                    )
                }
            }
        }

        return view
    }

    private fun createNC() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "mychannel"
            val description = "description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                "myChannelId",
                name,
                importance
            )
            notificationChannel.description = description
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    private fun showDateTimeDialog(date_time_in: TextView) {
        val calendar: Calendar = Calendar.getInstance()

        val dateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val timeSetListener =
                    OnTimeSetListener { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        val simpleDateFormat = SimpleDateFormat("EEE, d MMM  HH:mm")
                        selectedTime = simpleDateFormat.format(calendar.time)
                        notificationTime = calendar.time.time
                        date_time_in.text = selectedTime
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

    private fun getCurrentRequestCode(iContext: Context?): Int {
        val NOTIFICATION_ID_UPPER_LIMIT = 30000 // Arbitrary number.
        val NOTIFICATION_ID_LOWER_LIMIT = 0
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(iContext)
        val previousTokenId = sharedPreferences.getInt("currentNotificationTokenId", 0)
        val currentTokenId = previousTokenId + 1
        val editor = sharedPreferences.edit()
        if (currentTokenId < NOTIFICATION_ID_UPPER_LIMIT) {
            editor.putInt("currentNotificationTokenId", currentTokenId) // }
        } else {
            //If reaches the limit reset to lower limit..
            editor.putInt("currentNotificationTokenId", NOTIFICATION_ID_LOWER_LIMIT)
        }
        editor.commit()
        return currentTokenId
    }

}
