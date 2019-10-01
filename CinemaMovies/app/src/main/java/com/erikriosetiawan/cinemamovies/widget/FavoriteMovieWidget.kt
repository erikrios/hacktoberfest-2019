package com.erikriosetiawan.cinemamovies.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.erikriosetiawan.cinemamovies.R

/**
 * Implementation of App Widget functionality.
 */
class FavoriteMovieWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action.equals(TOAST_ACTION)) run {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched views " + viewIndex, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        private final const val TOAST_ACTION = "com.erikriosetiawan.cinemamovies.TOAST_ACTION"
        final const val EXTRA_ITEM = "com.erikriosetiawan.cinemamovies.EXTRA_ITEM"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, StackWidgetFavoriteMovieService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName,
                R.layout.favorite_movie_widget
            )
            views.setRemoteAdapter(R.id.stack_widget, intent)
            views.setEmptyView(
                R.id.stack_widget,
                R.id.empty_view
            )

            val toastIntent = Intent(context, FavoriteMovieWidget::class.java)
            toastIntent.setAction(TOAST_ACTION)
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
            val toastPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            views.setPendingIntentTemplate(R.id.stack_widget, toastPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

