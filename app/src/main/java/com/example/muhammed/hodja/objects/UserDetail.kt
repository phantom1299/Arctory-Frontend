package com.example.muhammed.hodja.objects

import org.json.JSONObject

class UserDetail {
    var name: String? = ""
    var photoUrl: String? = ""
    var startDate: String? = ""
    var finishDate: String? = ""

    constructor(name: String, photoUrl: String, startDate: String, finishDate: String = "") {
        this.name = name
        this.photoUrl = photoUrl
        this.startDate = startDate
        this.finishDate = finishDate
    }

    constructor(jsonObject: JSONObject) {
        name = getPropertyFromJsonObject<String>(jsonObject, "name")
        photoUrl = getPropertyFromJsonObject<String>(jsonObject, "photoUrl")
        startDate = getPropertyFromJsonObject<String>(jsonObject, "startDate")
        finishDate = getPropertyFromJsonObject<String>(jsonObject, "finishDate")
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
}