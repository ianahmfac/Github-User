package com.dicoding.githubuserv2.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserv2.database.MappingHelper
import com.dicoding.githubuserv2.model.Favorite

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null
    private var listFavorite: List<Favorite> = listOf()

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        cursor?.close()
        val identityToken = Binder.clearCallingIdentity()

        cursor = mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)
        cursor.let {
            listFavorite = MappingHelper.mapCursorToArrayList(it)
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val image = listFavorite[position].photoUser.toString()
        val bitmap = Glide.with(mContext.applicationContext).asBitmap()
            .load(image).submit(500, 500).get() as Bitmap
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(FavoriteUserWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int = listFavorite.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        cursor?.close()
        listFavorite = listOf()
    }
}