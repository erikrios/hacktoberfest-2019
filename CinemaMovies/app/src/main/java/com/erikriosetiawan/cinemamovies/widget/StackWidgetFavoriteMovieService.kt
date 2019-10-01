package com.erikriosetiawan.cinemamovies.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetFavoriteMovieService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewFavoriteMovieFactory(this.applicationContext)
    }

}