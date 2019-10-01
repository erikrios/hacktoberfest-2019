package com.erikriosetiawan.cinemamovies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var switchRelease: Switch
    private lateinit var switchDaily: Switch

    private var isDaily: Boolean = false
    private var isRelease: Boolean = false

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val editor: SharedPreferences.Editor =
            getSharedPreferences("com.erikriosetiawan.cinemamovies", Context.MODE_PRIVATE).edit()

        when (buttonView?.id) {
            R.id.switch_release_reminder -> {
                isRelease = switchRelease.isChecked
                editor.putBoolean("Release", isRelease)
                editor.apply()
            }
            R.id.switch_daily_reminder -> {
                isDaily = switchDaily.isChecked
                editor.putBoolean("Daily", isDaily)
                editor.apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initView()

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("com.erikriosetiawan.cinemamovies", Context.MODE_PRIVATE)
        switchRelease.isChecked = sharedPreferences.getBoolean("Release", false)
        switchDaily.isChecked = sharedPreferences.getBoolean("Daily", false)

        switchRelease.setOnCheckedChangeListener(this)
        switchDaily.setOnCheckedChangeListener(this)
    }

    private fun initView() {
        switchRelease = findViewById(R.id.switch_release_reminder)
        switchDaily = findViewById(R.id.switch_daily_reminder)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("Release", isRelease)
        outState.putBoolean("Daily", isDaily)
    }
}