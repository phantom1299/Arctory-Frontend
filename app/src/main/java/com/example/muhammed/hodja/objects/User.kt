package com.example.muhammed.hodja.objects

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject

class User {

    var _id: String
    var name: String
    var email: String?
    var phone: String?
    var photoUrl: String?
    var about: String?
    var languages: ArrayList<String>?
    var location: LatLng?

    constructor(jsonObject: JSONObject) {
        _id = getPropertyFromJsonObject<String>(jsonObject, "_id")!!
        name = getPropertyFromJsonObject<String>(jsonObject, "name")!!

        email = getPropertyFromJsonObject<String>(jsonObject, "email")
        phone = getPropertyFromJsonObject<String>(jsonObject, "phone")
        photoUrl = getPropertyFromJsonObject<String>(jsonObject, "photoUrl")
        about = getPropertyFromJsonObject<String>(jsonObject, "about")

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