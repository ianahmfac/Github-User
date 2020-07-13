package com.dicoding.githubuserv2.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.dicoding.githubuserv2"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_NAME = "name"
            const val COLUMN_PHOTO_USER = "photo_user"

            val CONTENT_URI: Uri =
                Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(TABLE_NAME).build()
        }
    }
}