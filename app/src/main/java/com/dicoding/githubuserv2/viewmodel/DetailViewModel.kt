package com.dicoding.githubuserv2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserv2.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String) {
        val listItem = ArrayList<User>()
        val url = "https://api.github.com/users/$username"

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

                    val userItem = User()
                    userItem.name = responseObject.getString("name")
                    userItem.company = responseObject.getString("company")
                    userItem.location = responseObject.getString("location")
                    userItem.repository = responseObject.getInt("public_repos")
                    userItem.followers = responseObject.getInt("followers")
                    userItem.following = responseObject.getInt("following")
                    listItem.add(userItem)

                    listUser.postValue(listItem)
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