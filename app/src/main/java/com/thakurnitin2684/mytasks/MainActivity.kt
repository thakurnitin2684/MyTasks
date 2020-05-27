package com.thakurnitin2684.mytasks

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_main.*

private const val TAG = "MainActivity"
internal const val DARK_FLAG = false

class MainActivity : AppCompatActivity(), MainActivityFragment.OnTaskEdit {
    private var mTwoPane = false
    private var aboutDialog: AlertDialog? = null
    private var darkFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        darkFlag = sharedPref.getBoolean(DARK_FLAG.toString(), false)

        if (darkFlag) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment != null) {
            // there was an existing fragment to edit a task, make sure the panes are set correctly
            showEditPane()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        } else {
            task_details_container.visibility = if (mTwoPane) View.VISIBLE else View.GONE
            mainFragment.view?.visibility = View.VISIBLE
        }
        fab.setOnClickListener {
            showEditPane()
            for (fragment in supportFragmentManager.fragments) {
                if (fragment != mainFragment)
                    supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            }
            val fragment = AddEditFragment()
            supportFragmentManager.beginTransaction().add(R.id.task_details_container, fragment)
                .commit()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }



        task_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                if (dy < 0) {
                    fab.show()
                } else if (dy > 0) {
                    fab.hide()
                }
            }
        })
    }

    private fun showEditPane() {
        task_details_container.visibility = View.VISIBLE
        mainFragment.view?.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }

    private fun removeEditPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane called")

        for (fragment in supportFragmentManager.fragments) {
            if (fragment != mainFragment)
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }

        task_details_container.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
        mainFragment.view?.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_about -> showInfoDialog()
            R.id.action_dark -> darkMode()
            android.R.id.home -> {
                removeEditPane()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun darkMode() {
        darkFlag = !darkFlag
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPref.edit().putBoolean(DARK_FLAG.toString(), darkFlag).apply()
        recreate()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment == null || mTwoPane) {
            super.onBackPressed()
        } else {
            removeEditPane(fragment)
        }

    }

    override fun onTaskEdit(task: Task) {
        showEditPane()
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != mainFragment)
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
        val bundle = Bundle()
        bundle.putParcelable("prevTask", task)
        val fragment = AddEditFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().add(R.id.task_details_container, fragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("InflateParams")
    private fun showInfoDialog() {
        val messgView = layoutInflater.inflate(R.layout.about, null, false)

        if (darkFlag) {
            val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)

            builder.setTitle(R.string.app_name)
            builder.setIcon(R.mipmap.ic_launcher)
            aboutDialog = builder.setView(messgView).create()
            aboutDialog?.setCanceledOnTouchOutside(true)
            val aboutVersion = messgView.findViewById(R.id.about_version) as TextView
            aboutVersion?.text = BuildConfig.VERSION_NAME
            aboutDialog?.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
            builder.setIcon(R.mipmap.ic_launcher)
            aboutDialog = builder.setView(messgView).create()
            aboutDialog?.setCanceledOnTouchOutside(true)
            val aboutVersion = messgView.findViewById(R.id.about_version) as TextView
            aboutVersion?.text = BuildConfig.VERSION_NAME
            aboutDialog?.show()
        }
    }
}
