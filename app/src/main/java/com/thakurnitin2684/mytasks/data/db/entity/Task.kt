package com.thakurnitin2684.mytasks.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Long,
    @ColumnInfo(name = "task_name")
    val name: String,
    @ColumnInfo(name = "task_description")
    val description: String,
    @ColumnInfo(name = "time")
    val time: String,
) : Parcelable

