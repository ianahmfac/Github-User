package com.dicoding.githubuserv2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.githubuserv2.database.DatabaseContract.AUTHORITY
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.githubuserv2.database.FavoriteHelper
import com.dicoding.githubuserv2.widget.FavoriteUserWidget

class UserProvider : ContentProvider() {

    companion object {
        const val USER = 1
        const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/username", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER -> cursor = favoriteHelper.queryAll()
            USER_ID -> cursor = favoriteHelper.querybyUsername(selection.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        refreshWidgetUser()
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteByUsername(selection.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        refreshWidgetUser()
        return deleted
    }

    private fun refreshWidgetUser() {
        // Refresh data in UserWidget
        FavoriteUserWidget.sendRefreshBroadcast(context!!)
    }
}
