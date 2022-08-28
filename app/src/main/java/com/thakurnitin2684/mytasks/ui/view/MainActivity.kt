package com.thakurnitin2684.mytasks.ui.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.thakurnitin2684.mytasks.BuildConfig
import com.thakurnitin2684.mytasks.R
import com.thakurnitin2684.mytasks.data.db.entity.Task
import com.thakurnitin2684.mytasks.data.repository.PrefRepository
import com.thakurnitin2684.mytasks.databinding.ActivityMainBinding
import com.thakurnitin2684.mytasks.databinding.ContentMainBinding
import com.thakurnitin2684.mytasks.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityFragment.OnTaskEdit {
    private var mTwoPane = false
    private var aboutDialog: AlertDialog? = null
    private  var darkFlag =false


    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var contentMainBinding: ContentMainBinding
    private lateinit var fragmentMainBinding: FragmentMainBinding



    private val   prefRepository by lazy { PrefRepository(applicationContext) }

    companion object {
        const val TASK_DATA = "TASK_DATA"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        darkFlag = prefRepository.getDarkBool(false)


        if (darkFlag) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        fragmentMainBinding = activityMainBinding.fragmentMainInclude

        contentMainBinding = activityMainBinding.contentMainInclude




        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment != null) {
            // there was an existing fragment to edit a task, make sure the panes are set correctly
            showEditPane()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        } else {

            contentMainBinding.taskDetailsContainer.visibility = if (mTwoPane) View.VISIBLE else View.GONE
            contentMainBinding.mainFragment.visibility = View.VISIBLE
        }


        activityMainBinding.fab.setOnClickListener {
           Log.d(TAG, "y<0")

            showEditPane()
            for (frag in supportFragmentManager.fragments) {
                if (frag != contentMainBinding.mainFragment.getFragment())
                    supportFragmentManager.beginTransaction().remove(frag!!).commit()
            }
            val frag = AddEditFragment()
            supportFragmentManager.beginTransaction().add(R.id.task_details_container, frag)
                .commit()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun showEditPane() {
        contentMainBinding.taskDetailsContainer.visibility = View.VISIBLE
        contentMainBinding.mainFragment.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }

    private fun removeEditPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane called")



        for (frag in supportFragmentManager.fragments) {
            if (frag != contentMainBinding.mainFragment.getFragment())
                supportFragmentManager.beginTransaction().remove(frag!!).commit()
        }


        replaceFragment()

        contentMainBinding.taskDetailsContainer.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
        contentMainBinding.mainFragment.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }
     private fun replaceFragment(){
         supportFragmentManager.beginTransaction().replace(
             R.id.main_fragment,MainActivityFragment()).addToBackStack(null).commit()
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
       Log.d(TAG, "$darkFlag")
        prefRepository.setDarkBool(darkFlag)
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
            if (fragment != contentMainBinding.mainFragment.getFragment())
                supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
        val bundle = Bundle()
        bundle.putParcelable(TASK_DATA, task)
        val fragment = AddEditFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().add(R.id.task_details_container, fragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRecyclerViewScroll(y : Int) {
        if (y < 0) {
            activityMainBinding.fab.show()
            Log.d(TAG, "y<0")

        } else if (y > 0) {
            activityMainBinding.fab.hide()
            Log.d(TAG, "y<0")

        }

    }

    @SuppressLint("InflateParams")
    private fun showInfoDialog() {
        val messageView = layoutInflater.inflate(R.layout.about, null, false)

        val builder : AlertDialog.Builder = if (darkFlag) {
            AlertDialog.Builder(this, R.style.MyDialogTheme)

        } else {
            AlertDialog.Builder(this)

        }
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.new_icon)
        aboutDialog = builder.setView(messageView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)
        val aboutVersion = messageView.findViewById(R.id.about_version) as TextView
        aboutVersion.text = BuildConfig.VERSION_NAME
        aboutDialog?.show()
    }


}
