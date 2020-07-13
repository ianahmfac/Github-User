package com.dicoding.githubuserv2.database

import android.database.Cursor
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_NAME
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_PHOTO_USER
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.githubuserv2.model.Favorite

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val photo = getString(getColumnIndexOrThrow(COLUMN_PHOTO_USER))

                favoriteList.add(Favorite(username, name, photo))
            }
        }
        return favoriteList
    }
}