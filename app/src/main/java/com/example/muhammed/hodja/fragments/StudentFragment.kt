package com.example.muhammed.hodja.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView

import com.example.muhammed.hodja.R
import com.example.muhammed.hodja.UserAdapter
import com.example.muhammed.hodja.UserGridViewAdapter
import com.example.muhammed.hodja.objects.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONArray
import org.json.JSONObject


class StudentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val args = arguments
        val _id = args.getString("_id")
        getData(_id)

        val v = inflater!!.inflate(R.layout.fragment_student, container, false)



        // Inflate the layout for this fragment
        return v
    }

    fun getData(_id: String) {
        // Başvuruları çek
        Fuel.get(getString(R.string.applicationsUrl) + "?userId=$_id").responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultArray = result.value.array() //JSONObj
                    populateApplicants(resultArray)
                }
            }
        }
        // Öğrencileri Çek
        Fuel.get(getString(R.string.usersUrl) + "/5a87f747a650a9d8641348fc/students").responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultArray = result.value.array() //JSONObj
                    populateStudents(resultArray)
                }
            }
        }
    }

    fun populateApplicants(jsonArray: JSONArray) {
        val applicantIds = ArrayList<String>()
        val lessonIds = ArrayList<String>()
        val studentIds = ArrayList<String>()
        val applicants = ArrayList<User>()
        for (i in 0..(jsonArray.length() - 1)) {
            val applicationId = (jsonArray.get(i) as JSONObject).getString("_id")
            val lessonId = (jsonArray.get(i) as JSONObject).getString("lesson")
            val studentId = (jsonArray.get(i) as JSONObject).getString("student")

            val applicant = User(studentId, activity)
            applicants.add(applicant)
            applicantIds.add(applicationId)
            lessonIds.add(lessonId)
            studentIds.add(studentId)
        }
        val gridView = activity.findViewById<GridView>(R.id.applicationGridView)
        val adapter2 = UserGridViewAdapter(activity, applicants, applicantIds, lessonIds, studentIds)

        gridView.adapter = adapter2

        adapter2.notifyDataSetChanged()
    }

    fun populateStudents(jsonArray: JSONArray) {
        val students = ArrayList<User>()

        for (i in 0..jsonArray.length() - 1) {
            val obj = jsonArray.get(i) as JSONObject

            val studentsJsonArray = obj.getJSONArray("students")

            for (j in 0..(studentsJsonArray.length() - 1)) {
                val studentJsonObj = studentsJsonArray.getJSONObject(j)
                val student = User(studentJsonObj)
                students.add(student)
            }
        }

        val listView = activity.findViewById<ListView>(R.id.userListView)
        val adapter = UserAdapter(activity, students)

        listView.adapter = adapter

        adapter.notifyDataSetChanged()
    }

}// Required empty public constructor
