package com.dicoding.githubuserv2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.model.UserFollow
import kotlinx.android.synthetic.main.item_followers_following.view.*

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    private val mData = ArrayList<UserFollow>()
    private var onItemClickCallBack: FollowAdapter.OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: UserFollow)
    }

    fun setOnItemClickCallback(onItemClickCallback: FollowAdapter.OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }

    fun setData(items: ArrayList<UserFollow>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follow: UserFollow) {
            with(itemView) {
                Glide.with(itemView.context).load(follow.photoFollow)
                    .apply(RequestOptions().override(250, 250)).into(imgPhoto)

                tvUsername.text = follow.usernameFollow

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(follow) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_followers_following, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FollowAdapter.ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }
}