package com.android.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.search.data.User
import com.android.search.gituserdata.GitUserData
import com.android.search.userlist.UserAdapter
import java.io.FileNotFoundException

private const val TAG = "MainActivity"
const val FINISH_USER_DATA = 1
const val CLEAR_USER_DATA = 2
const val ERROR_USER_DATA = 3

class MainActivity : AppCompatActivity() {
    val gitUserData = GitUserData()
    val userAdapter = UserAdapter(this)
    var userList: ArrayList<User> = ArrayList()
    var lockThread:Boolean = true
    var firstSearch: Boolean = true
    var searchPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val editText: EditText = findViewById(R.id.header_edit)
        val imageView: ImageView = findViewById(R.id.imageView)
        recyclerView.adapter = userAdapter

        val handler = Handler(){
            if(it.what == FINISH_USER_DATA){
                userAdapter.addUserList(userList)
                userAdapter.submitList(userList as MutableList<User>)
            }else if(it.what == CLEAR_USER_DATA){
                userAdapter.deleteUserList(userList)
                userAdapter.submitList(userList as MutableList<User>)
            }else if(it.what == ERROR_USER_DATA){
                Log.i(TAG, "Error User Limit.")
                Toast.makeText(this, R.string.limite_error, Toast.LENGTH_SHORT).show()
            }
            true
        }

        imageView.setOnClickListener(){
            initSearchParameter()
            Thread{
                if(editText.getText().toString() != ""){
                    if(!firstSearch) {
                        val clear = Message()
                        userList.clear()
                        clear.what = CLEAR_USER_DATA
                        handler.sendMessage(clear)
                    }
                    while(true) {
                        if ((userAdapter.getUserPosition() + 1) % 100 == 0) {
                            lockThread = true
                            searchPage++
                        }
                        if (lockThread) {
                            val update = Message()
                            val Error = Message()
                            gitUserData.setsearchString(editText.getText().toString())
                            gitUserData.setsearchPage(Integer.toString(searchPage))
                            try {
                                userList = gitUserData.getQuaryData()
                            }catch(e: FileNotFoundException){
                                Error.what = ERROR_USER_DATA
                                handler.sendMessage(Error)
                                e.printStackTrace()
                            }
                            update.what = FINISH_USER_DATA
                            handler.sendMessage(update)
                            lockThread = false
                            firstSearch = false
                        }
                        Thread.sleep(200)
                    }
                }
            }.start()
        }
    }

    fun initSearchParameter(){
        lockThread = true
        searchPage = 1
    }
}