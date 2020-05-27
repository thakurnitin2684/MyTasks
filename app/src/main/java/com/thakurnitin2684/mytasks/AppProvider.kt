package com.thakurnitin2684.mytasks

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
credits for code base : Tim Buchelka ,www.learnprogramming.academy
 */
private const val TAG = "AppProvider"
const val CONTENT_AUTHORITY = "com.thakurnitin2684.mytasks.provider"
private const val TASKS = 10
private const val TASKS_ID = 11
val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class AppProvider : ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(CONTENT_AUTHORITY, TasksTable.TABLE_NAME, TASKS)
        matcher.addURI(CONTENT_AUTHORITY, "${TasksTable.TABLE_NAME}/#", TASKS_ID)
        return matcher
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate starts")
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: Called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: match is  $match")

        val queryBuilder = SQLiteQueryBuilder()
        when (match) {
            TASKS -> queryBuilder.tables = TasksTable.TABLE_NAME

            TASKS_ID -> {
                queryBuilder.tables = TasksTable.TABLE_NAME
                val taskId = TasksTable.getId(uri)
                queryBuilder.appendWhere("${TasksTable.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$taskId")
            }


            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        val db = AppDatabase.getInstance(context!!).readableDatabase
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "Insert: Called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "Insert: match is  $match")

        val recordId: Long
        val returnUri: Uri
        when (match) {
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(TasksTable.TABLE_NAME, null, values)
                if (recordId != -1L) {
                    returnUri = TasksTable.buildUriFromId(recordId)
                } else {
                    throw SQLException("FAiled to insert ,Uri was $uri")
                }

            }
            else -> throw IllegalArgumentException("UnkownUri: $uri")
        }
        if (recordId > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        Log.d(TAG, "Exiting insert ,returning $returnUri")
        return returnUri
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "Update: Called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "Update: match is  $match")

        val count: Int
        var selectionCriteria: String
        when (match) {
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(TasksTable.TABLE_NAME, values, selection, selectionArgs)

            }
            TASKS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TasksTable.getId(uri)
                selectionCriteria = "${TasksTable.Columns.ID}= $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(TasksTable.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            else -> throw IllegalArgumentException("Unkown uri:$uri")
        }
        if (count > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "Delete: Called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "Delete: match is  $match")

        val count: Int
        var selectionCriteria: String
        when (match) {
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.delete(TasksTable.TABLE_NAME, selection, selectionArgs)

            }
            TASKS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TasksTable.getId(uri)
                selectionCriteria = "${TasksTable.Columns.ID}= $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(TasksTable.TABLE_NAME, selectionCriteria, selectionArgs)
            }


            else -> throw IllegalArgumentException("Unkown uri:$uri")
        }
        if (count > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        Log.d(TAG, "Exiting delete, returning $count")
        return count
    }
}