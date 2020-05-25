package com.thakurnitin2684.mytasks

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), MainActivityFragment.OnTaskEdit {
    private var mTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment != null) {
            // there was an existing fragment to edit a task, make sure the panes are set correctly
            showEditPane()
        } else {
            task_details_container.visibility = if (mTwoPane) View.VISIBLE else View.GONE
            mainFragment.view?.visibility = View.VISIBLE
        }
        fab.setOnClickListener {
            showEditPane()
            for (fragment in supportFragmentManager.fragments) {
                if(fragment!= mainFragment)
                    supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            }
            val fragment = AddEditFragment()
            supportFragmentManager.beginTransaction().add(R.id.task_details_container, fragment)
                .commit()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showEditPane() {
        task_details_container.visibility = View.VISIBLE
        mainFragment.view?.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }

    private fun removeEditPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane called")

            if (fragment != null) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
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
            R.id.action_settings -> true
            android.R.id.home -> {
                removeEditPane()
            }
        }
        return super.onOptionsItemSelected(item)
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
            if(fragment!= mainFragment)
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
        val bundle = Bundle()
        bundle.putParcelable("prevTask", task)
        val fragment = AddEditFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().add(R.id.task_details_container, fragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        replaceFragment(AddEditFragment.newInstance(task), R.id.task_details_container)
    }


//private fun FragmentActivity.replaceFragment(fragment: Fragment, frameID: Int) {
//    supportFragmentManager.beginTransaction().replace(frameID, fragment).commit()
//}

}
