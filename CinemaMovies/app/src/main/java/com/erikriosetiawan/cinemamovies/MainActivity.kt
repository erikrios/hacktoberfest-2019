package com.erikriosetiawan.cinemamovies

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erikriosetiawan.cinemamovies.alarm.AlarmReceiver
import com.erikriosetiawan.cinemamovies.widget.UpdateWidgetFavoriteMovieService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val movieJobId = 100
    val SCHEDULE_OF_PERIOD = 1000L

    companion object {
        const val SETTING_REQUEST_CODE = 1
    }

    private lateinit var navView: BottomNavigationView

    private lateinit var alarmReceiver: AlarmReceiver
    private var isDaily = false
    private var isRelease = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movie, R.id.navigation_tv_show, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        startMovieJob()

        alarmReceiver = AlarmReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchMovie: MenuItem? = menu?.findItem(R.id.item_search_movie)
        val searchViewMovie: SearchView = searchMovie?.actionView as SearchView
        searchViewMovie.queryHint = resources.getString(R.string.search_movie)
        searchViewMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sendQueryIntent = Intent(this@MainActivity, SearchActivity::class.java)
                sendQueryIntent.putExtra(
                    SearchActivity.SEARCH_KEY,
                    SearchActivity.SEARCH_MOVIE_KEY
                )
                sendQueryIntent.putExtra(SearchActivity.SEARCH_MOVIE_KEY, query)
                startActivity(sendQueryIntent)
                Log.d("TES123", "Query berada di Movie")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        val searchTvShow: MenuItem? = menu?.findItem(R.id.item_search_tv_show)
        val searchViewTvShow: SearchView = searchTvShow?.actionView as SearchView
        searchViewTvShow.queryHint = getString(R.string.search_tv_show)
        searchViewTvShow.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sendQueryIntent = Intent(this@MainActivity, SearchActivity::class.java)
                sendQueryIntent.putExtra(
                    SearchActivity.SEARCH_KEY,
                    SearchActivity.SEARCH_TV_SHOW_KEY
                )
                sendQueryIntent.putExtra(SearchActivity.SEARCH_TV_SHOW_KEY, query)
                startActivity(sendQueryIntent)
                Log.d("TES123", "Query berada di Tv Show")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.action_change_language -> {
                    val changeLanguageIntent = Intent()
                    changeLanguageIntent.action = Settings.ACTION_LOCALE_SETTINGS
                    startActivity(changeLanguageIntent)
                }
                R.id.action_setting -> {
                    val settingIntent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivityForResult(settingIntent, SETTING_REQUEST_CODE)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startMovieJob() {
        val mServiceComponenMovie =
            ComponentName(this, UpdateWidgetFavoriteMovieService::class.java)
        val builder: JobInfo.Builder = JobInfo.Builder(movieJobId, mServiceComponenMovie)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) builder.setMinimumLatency(
            SCHEDULE_OF_PERIOD
        ) else builder.setPeriodic(SCHEDULE_OF_PERIOD)
        val jobScheduler: JobScheduler =
            getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
        Log.d("TES123", "Job Service Dimulai")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTING_REQUEST_CODE) {
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("com.erikriosetiawan.cinemamovies", Context.MODE_PRIVATE)
            isRelease = sharedPreferences.getBoolean("Release", false)
            isDaily = sharedPreferences.getBoolean("Daily", false)

            if (isRelease) alarmReceiver.setRepeatingAlarm(
                this,
                AlarmReceiver.TYPE_RELEASE,
                "20:42",
                "Release Alarm",
                AlarmReceiver.ID_RELEASE
            ) else alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_RELEASE)

            if (isDaily) alarmReceiver.setRepeatingAlarm(
                this,
                AlarmReceiver.TYPE_DAILY,
                "20:38",
                getString(R.string.daily_reminder_message),
                AlarmReceiver.ID_DAILY
            ) else alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_DAILY)
        }
    }
}
