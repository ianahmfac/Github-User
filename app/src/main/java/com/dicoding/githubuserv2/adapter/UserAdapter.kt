package com.dicoding.githubuserv2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val mData = ArrayList<User>()

    private var onItemClickCallBack: OnItemClickCallback? = null

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context).load(user.photo)
                    .apply(RequestOptions().override(250, 250)).into(imgPhoto)
                tvUsername.text = user.username

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(user) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserAdapter.ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}