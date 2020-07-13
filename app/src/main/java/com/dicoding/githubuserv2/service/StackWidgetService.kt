package com.dicoding.githubuserv2.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.dicoding.githubuserv2.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)

}