package com.thakurnitin2684.mytasks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(val name: String, val description: String, val time: String, var id:Long=0  ):
    Parcelable