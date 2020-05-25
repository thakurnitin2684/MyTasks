package com.thakurnitin2684.mytasks

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
credits for code base : Tim Buchelka ,www.learnprogramming.academy
 */
private const val TAG = "AppDatabase"
private const val DATABASE_NAME = "mytasks.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate: starts")
        val sSQL = """ CREATE TABLE ${TasksTable.TABLE_NAME}(
        ${TasksTable.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
        ${TasksTable.Columns.TASK_NAME} TEXT NOT NULL,
        ${TasksTable.Columns.TASK_DESCRIPTION} TEXT ,
        ${TasksTable.Columns.TASK_TIME} INTEGER);""".replaceIndent(" ")
        Log.d(TAG, sSQL)
        db.execSQL(sSQL)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG,"onUgrade : Starts")
        when(oldVersion){
            1->{
                //on Upgrade logic
            }

        else-> throw IllegalStateException("onUpgrade() with unknown newversion: $newVersion")
        }
    }
    companion object : SingletonHolder<AppDatabase,Context>(::AppDatabase)
}