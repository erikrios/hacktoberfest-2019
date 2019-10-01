package com.erikriosetiawan.cinemamovies.widget

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class UpdateWidgetFavoriteMovieService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        val intent = Intent(this, FavoriteMovieWidget::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)

        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, FavoriteMovieWidget::class.java))

        val favoriteMovieWidget = FavoriteMovieWidget()
        favoriteMovieWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids)
        jobFinished(params, false)
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        return false
    }

}