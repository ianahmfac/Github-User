package com.dicoding.githubuserv2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String?) {
        val listItem = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=${username}"

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
                    val responseObject = JSONObject(result)
                    if (responseObject.getInt("total_count") == 0) {
                        val userItem = User()
                        userItem.userNotFound = R.string.notFound.toString()
                        listItem.add(userItem)
                        listUser.postValue(listItem)
                    } else {
                        val items = responseObject.getJSONArray("items")

                        for (i in 0 until items.length()) {
                            val user = items.getJSONObject(i)
                            val userItem = User()
                            userItem.username = user.getString("login")
                            userItem.photo = user.getString("avatar_url")
                            listItem.add(userItem)
                        }
                        listUser.postValue(listItem)
                    }
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

    fun getUser(): LiveData<ArrayList<User>> {
        return listUser
    }
}