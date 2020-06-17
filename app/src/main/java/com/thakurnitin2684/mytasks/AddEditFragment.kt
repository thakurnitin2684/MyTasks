package com.thakurnitin2684.mytasks

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
import com.thakurnitin2684.mytasks.ReminderBroadcast.Companion.TASK_DES
import com.thakurnitin2684.mytasks.ReminderBroadcast.Companion.TASK_NAME
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.android.synthetic.main.edit_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "AddEditFragment"

class AddEditFragment : Fragment() {
    private var time = ""
    private var time2 = ""
    private var timeBool = false
    private var notificationTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.edit_fragment, container, false)
        val viewModel: MyTasksViewModel by activityViewModels()
        createNC()
        Log.d(TAG, "onCreateView started")
        var tID = 0
        if (arguments != null) {
            val strtTask = requireArguments().getParcelable<Task>("prevTask")
            if (strtTask != null) {
                root.edit_Title.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.name)
                root.edit_Description.editText?.text =
                    Editable.Factory.getInstance().newEditable(strtTask.description)
                time = strtTask.time
                tID = strtTask.id.toInt()
                root.edit_show_time.text = time
            }
        }

        root.edit_time.setOnClickListener {
            showDateTimeDialog(edit_show_time)
            timeBool = true
        }
        root.edit_save.setOnClickListener {
            val title = root.edit_Title.editText?.text.toString()
            val description = root.edit_Description.editText?.text.toString()
            val task = Task(
                title, description, if (timeBool) time2 else time, tID.toLong()
            )
            viewModel.saveTask(task)
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity?.onBackPressed()
            } else {
                Toast.makeText(context, "Your Task is saved", Toast.LENGTH_LONG).show()
            }
            Log.d(TAG, "task : $task")
            if (timeBool) {
                val intent = Intent(requireActivity(), ReminderBroadcast::class.java)
                intent.putExtra(TASK_NAME, title)
                intent.putExtra(TASK_DES, description)

                val pendingIntent = PendingIntent.getBroadcast(
                    context, getCurrentRequestCode(
                        context?.applicationContext
                    ), intent, 0
                )
                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                if (alarmManager != null) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent)
                }
            }
        }
        return root
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
            OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val timeSetListener =
                    OnTimeSetListener { view, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        val simpleDateFormat = SimpleDateFormat("EEE, d MMM  HH:mm")
                        time2 = simpleDateFormat.format(calendar.time)
                        notificationTime = calendar.time.time
                        date_time_in.text = time2
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
