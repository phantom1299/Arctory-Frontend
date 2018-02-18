package com.example.muhammed.hodja

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import com.example.muhammed.hodja.objects.User
import com.example.muhammed.hodja.objects.UserDetailsAdapter
import com.example.muhammed.hodja.objects.UserDetailsLanguageAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_user_details.*
import org.json.JSONArray
import org.json.JSONObject

class UserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        val _id = intent.getStringExtra("_id")
        getUserDetails(_id)
    }

    fun getUserDetails(_id: String) {
        Fuel.get(getString(R.string.usersUrl) + "/$_id").responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultObj = result.value.obj() //JSONObj
                    handleResponse(resultObj)
                }
            }
        }
        Fuel.get(getString(R.string.usersUrl) + "/$_id/lessonsGiven").responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultArray = result.value.array() //JSONObj
                    val lessonsGiven = convertJsonArrayToArrayList(resultArray)

                    val lessonListView = findViewById<ListView>(R.id.lessonListView)
                    val lessonAdapter = UserDetailsLanguageAdapter(this, lessonsGiven)
                    lessonListView.adapter = lessonAdapter
                    lessonAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun convertJsonArrayToArrayList(jsonArray: JSONArray): ArrayList<String> {
        val result = ArrayList<String>()
        for (i in 0..jsonArray.length() - 1) {
            val lesson = jsonArray.get(i) as JSONObject
            result.add(lesson.getString("name"))
        }

        return result;
    }

    fun handleResponse(jsonObject: JSONObject) {
        val user = User(jsonObject)

        val nameLabel = findViewById<TextView>(R.id.userNameLabel)
        nameLabel.text = user.name

        // val profilePhoto = findViewById<ImageView>(R.id.userProfilePhoto)
        // profilePhoto.setImageURI(Uri.parse(user.photoUrl))

        val aboutLabel = findViewById<TextView>(R.id.aboutLabel)
        aboutLabel.text = user.about

        val expListView = findViewById<ListView>(R.id.expListView)
        val expAdapter = UserDetailsAdapter(this, user.experiences)
        expListView.adapter = expAdapter
        expAdapter.notifyDataSetChanged()

        val eduListView = findViewById<ListView>(R.id.eduListView)
        val eduAdapter = UserDetailsAdapter(this, user.education)
        eduListView.adapter = eduAdapter
        eduAdapter.notifyDataSetChanged()

        val langListView = findViewById<ListView>(R.id.languagesListView)
        val langAdapter = UserDetailsLanguageAdapter(this, user.languages)
        langListView.adapter = langAdapter
        langAdapter.notifyDataSetChanged()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
