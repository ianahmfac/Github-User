package com.dicoding.consumerapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.consumerapp.model.UserFollow
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<UserFollow>>()

    fun setFollowers(username: String) {
        val listItem = ArrayList<UserFollow>()
        val url = "https://api.github.com/users/${username}/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token cd528d142cef751f32265868bf1e7b0f296b8829")
        client.addHeader("User-agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val followers = responseArray.getJSONObject(i)
                        val userFollowers = UserFollow()
                        userFollowers.usernameFollow = followers.getString("login")
                        userFollowers.photoFollow = followers.getString("avatar_url")
                        listItem.add(userFollowers)
                    }

                    listFollowers.postValue(listItem)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<UserFollow>> {
        return listFollowers
    }
}