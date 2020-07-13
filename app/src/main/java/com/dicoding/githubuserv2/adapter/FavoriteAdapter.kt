package com.dicoding.githubuserv2.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserv2.database.MappingHelper
import com.dicoding.githubuserv2.model.Favorite
import kotlinx.android.synthetic.main.item_user_favorite.view.*

class FavoriteAdapter(val context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var onItemClickCallBack: FavoriteAdapter.OnItemClickCallback? = null

    private fun getData(): ArrayList<Favorite> {
        val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor
        return MappingHelper.mapCursorToArrayList(cursor)
    }

    var listFavorite = getData()

    fun setOnItemClickCallback(onItemClickCallback: FavoriteAdapter.OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)

    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite) {
            with(itemView) {
                tvUsername.text = favorite.username
                tvName.text = favorite.name
                Glide.with(itemView.context).load(favorite.photoUser)
                    .apply(RequestOptions().override(250, 250)).into(imgPhoto)

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(favorite) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
}