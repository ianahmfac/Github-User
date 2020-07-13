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

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<UserFollow>>()

    fun setFollowing(username: String) {
        val listItem = ArrayList<UserFollow>()
        val url = "https://api.github.com/users/${username}/following"
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
                        val following = responseArray.getJSONObject(i)
                        val userFollowing = UserFollow()
                        userFollowing.usernameFollow = following.getString("login")
                        userFollowing.photoFollow = following.getString("avatar_url")
                        listItem.add(userFollowing)
                    }

                    listFollowing.postValue(listItem)
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

    fun getFollowing(): LiveData<ArrayList<UserFollow>> {
        return listFollowing
    }
}