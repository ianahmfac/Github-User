package com.dicoding.githubuserv2.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.service.StackWidgetService
import com.dicoding.githubuserv2.ui.favorite.FavoriteActivity

class FavoriteUserWidget : AppWidgetProvider() {

    companion object {

        const val EXTRA_ITEM = "com.dicoding.githubuserv2.EXTRA_ITEM"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_user_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val favoriteIntent = Intent(context, FavoriteActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                favoriteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                component = ComponentName(context, FavoriteUserWidget::class.java)
            }
            context.sendBroadcast(intent)
        }
    }

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

    override fun onReceive(context: Context?, intent: Intent) {
        intent.let {
            if (it.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
                val component = context?.let {
                    ComponentName(context, FavoriteUserWidget::class.java)
                }
                AppWidgetManager.getInstance(context).apply {
                    notifyAppWidgetViewDataChanged(getAppWidgetIds(component), R.id.stack_view)
                }
            }
        }
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}