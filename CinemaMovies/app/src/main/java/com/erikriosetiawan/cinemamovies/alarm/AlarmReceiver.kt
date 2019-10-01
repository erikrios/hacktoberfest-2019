package com.erikriosetiawan.cinemamovies.alarm

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.erikriosetiawan.cinemamovies.BuildConfig
import com.erikriosetiawan.cinemamovies.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY

        const val TYPE_DAILY = "Daily Reminder"
        const val TYPE_RELEASE = "Release Reminder"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val ID_DAILY = 101
        const val ID_RELEASE = 100
    }

    val TIME_FORMAT = "HH:mm"

    override fun onReceive(context: Context?, intent: Intent?) {
        val type: String = intent!!.getStringExtra(EXTRA_TYPE)
        val message: String = intent.getStringExtra(EXTRA_MESSAGE)

        val title: String = if (type.equals(TYPE_RELEASE, true)) TYPE_RELEASE else TYPE_DAILY
        val notifId: Int = if (type.equals(TYPE_RELEASE, true)) ID_RELEASE else ID_DAILY

        if (notifId == ID_RELEASE) context?.let { releaseMovie(it) } else context?.let {
            showAlarmNotification(
                it,
                title,
                message,
                notifId
            )
        }
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val CHANNEL_ID = "Channe_1"
        val CHANNEL_NAME = "Alarm Manager Channel"

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)

            if (notificationManager != null) notificationManager.createNotificationChannel(channel)
        }
        val notification: Notification = builder.build()
        if (notificationManager != null) notificationManager.notify(notifId, notification)
    }

    @SuppressLint("SimpleDateFormat")
    private fun releaseMovie(context: Context) {
        val date: Date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateRelease = dateFormat.format(date)
        val dateNow = dateFormat.format(date)

        val client = AsyncHttpClient()
        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$dateRelease&primary_release_date.lte=$dateRelease"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    val list: JSONArray = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie: JSONObject? = list.getJSONObject(i)
                        val releaseDate = movie?.getString("release_date")

                        if (releaseDate.equals(dateNow)) {
                            showAlarmNotification(
                                context,
                                movie!!.getString("title"),
                                movie.getString("title") + " is coming!",
                                movie.getInt("id")
                            )
                            Log.d("Success", movie.getString("title"))
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message)
            }

        })
    }

    fun isDateInvalid(date: String, format: String): Boolean {
        try {
            val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(date)
            return false
        } catch (e: ParseException) {
            return true
        }
    }

    fun setRepeatingAlarm(
        context: Context,
        type: String,
        time: String,
        message: String,
        requestCode: Int
    ) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)
        val timeArray: List<String> = time.split(":")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, 0)
        if (alarmManager != null)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        Toast.makeText(context, "$type set up at $time", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_RELEASE, ignoreCase = true)) ID_RELEASE else ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        if (alarmManager != null) alarmManager.cancel(pendingIntent)
        Toast.makeText(context, type + " Stopped", Toast.LENGTH_SHORT).show()
    }
}