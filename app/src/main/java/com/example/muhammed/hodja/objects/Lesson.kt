package com.example.muhammed.hodja.objects

import org.json.JSONObject

class Lesson {

    var _id: String
    var name: String
    var teacher: User? = null
    var students = ArrayList<User>()

    constructor(jsonObject: JSONObject) {
        _id = jsonObject.getString("_id")
        name = jsonObject.getString("name")

        val userJson = jsonObject.getJSONObject("teacher")

        if (userJson != null) {
            teacher = User(userJson)
        }
    }


}