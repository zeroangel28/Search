package com.android.search.gituserdata

import android.util.Log
import com.android.search.data.User
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

private const val TAG = "GitUserData"

class GitUserData (){
    private val userList: ArrayList<User> = ArrayList()
    private var searchString: String = ""
    private var searchPage: String = "1"

    fun setsearchString(searchString: String){
        this.searchString = searchString
    }

    fun setsearchPage(searchPage: String){
        this.searchPage = searchPage
    }

    private fun generateQuaryURL() : String {
        var quaryURL : String = "https://api.github.com/search/users?q=" + searchString + "&page="+ searchPage +"&per_page=100"
        Log.i(TAG, quaryURL)
        return quaryURL
    }

    fun getQuaryData(): ArrayList<User>{
        var quaryURL = URL(generateQuaryURL()).readText()
        val json_contact: JSONObject = JSONObject(quaryURL)
        val jsonarray_info: JSONArray = json_contact.getJSONArray("items")
        var size:Int = jsonarray_info.length()
        for (i in 0.. size-1) {
            var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
            var user:User = User(json_objectdetail.getString("login"),json_objectdetail.getString("avatar_url"));
            userList.add(user)
        }
        return userList
    }

    fun clearArrayList(): ArrayList<User>{
        userList.clear()
        return userList
    }

}