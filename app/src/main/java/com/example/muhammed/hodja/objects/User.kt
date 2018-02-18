package com.example.muhammed.hodja.objects

import android.app.Activity
import com.example.muhammed.hodja.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject

class User {

    var _id: String = ""
    var name: String = ""
    var email: String? = null
    var phone: String? = null
    var photoUrl: String? = null
    var about: String? = null
    var education: ArrayList<UserDetail>? = null
    var experiences: ArrayList<UserDetail>? = null
    var languages: ArrayList<String>? = null
    var location: LatLng? = null

    constructor(_id: String, activity: Activity) {
        Fuel.get(activity.getString(R.string.usersUrl) + "/$_id").responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultObj = result.value.obj() //JSONObj


                    this._id = getPropertyFromJsonObject<String>(resultObj, "_id")!!
                    name = getPropertyFromJsonObject<String>(resultObj, "name")!!

                    //initializeUser(resultObj)
                }
            }
        }
    }

    constructor(_id: String, name: String, photoUrl: String) {
        this._id = _id
        this.name = name
        this.photoUrl = photoUrl
    }

    constructor(jsonObject: JSONObject) {
        initializeUser(jsonObject)
    }

    fun initializeUser(jsonObject: JSONObject) {
        _id = getPropertyFromJsonObject<String>(jsonObject, "_id")!!
        name = getPropertyFromJsonObject<String>(jsonObject, "name")!!

        email = getPropertyFromJsonObject<String>(jsonObject, "email")
        phone = getPropertyFromJsonObject<String>(jsonObject, "phone")
        photoUrl = getPropertyFromJsonObject<String>(jsonObject, "photoUrl")
        about = getPropertyFromJsonObject<String>(jsonObject, "about")

        experiences = convertJSONArrayToObjectArray(
                getPropertyFromJsonObject<JSONArray>(jsonObject, "experiences"))

        education = convertJSONArrayToObjectArray(
                getPropertyFromJsonObject<JSONArray>(jsonObject, "education"))

        languages = convertJSONArrayToArrayList(
                getPropertyFromJsonObject<JSONArray>(jsonObject, "languages"))

        location = convertJSONArrayToLatLng(
                getPropertyFromJsonObject<JSONArray>(jsonObject, "location"))
    }

    fun convertJSONArrayToArrayList(jsonArr: JSONArray?): ArrayList<String>? {
        if (jsonArr == null)
            return null

        val result = ArrayList<String>()

        for (i in 0..jsonArr.length() - 1) {
            result.add(jsonArr.get(i) as String)
        }

        return result
    }

    fun convertJSONArrayToObjectArray(jsonArr: JSONArray?): ArrayList<UserDetail>? {
        if (jsonArr == null)
            return null

        val result = ArrayList<UserDetail>()

        for (i in 0..jsonArr.length() - 1) {
            result.add(UserDetail(jsonArr.get(i) as JSONObject))
        }

        return result
    }

    fun <T> getPropertyFromJsonObject(jsonObject: JSONObject, key: String): T? {
        val property: T

        try {
            property = jsonObject.get(key) as T
        } catch (ex: Exception) {
            return null
        }

        return property
    }

    fun convertJSONArrayToLatLng(jsonArr: JSONArray?): LatLng? {
        if (jsonArr == null)
            return null

        val lat = jsonArr.get(0) as Double
        val lng = jsonArr.get(1) as Double

        return LatLng(lat, lng)
    }


}