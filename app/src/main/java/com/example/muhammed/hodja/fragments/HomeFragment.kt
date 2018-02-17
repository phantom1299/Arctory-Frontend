package com.example.muhammed.hodja.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.muhammed.hodja.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val pp = LatLng(41.080930, 29.030098)
        val option = MarkerOptions()
        option.position(pp).title("Başlangıç")
        map!!.addMarker(option)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(pp))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(pp, 15.0f))
        Log.d("Result", "Here")
        getData()
    }

    fun getData() {
        Fuel.get(getString(R.string.lessonsUrl), listOf("maxFee" to "1000", "maxDistance" to "500", "studentLocation" to "[41.080930,29.030098]")).responseJson { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    textView3.text = result.toString()
                }
                is Result.Success -> {
                    val resultObj = result.value.obj() //JSONObj
                    registerMarkers(resultObj.getJSONArray("result"))
                }
            }
        }
    }

    private fun registerMarkers(jsonArray: JSONArray) {
        var option: MarkerOptions
        for (i in 0..jsonArray.length() - 1) {
            val jsonObj = jsonArray[i] as JSONObject

            val lessonName = jsonObj.get("name")

            val teacherObj = jsonObj.get("teacher") as JSONObject

            val lat = teacherObj.getJSONArray("location").get(0) as Double
            val long = teacherObj.getJSONArray("location").get(1) as Double

            val teacherName = teacherObj.get("name") as String
            val location = LatLng(lat, long)



            option = MarkerOptions()
            option.position(location).title(teacherName)
            map!!.addMarker(option)
        }
    }

}// Required empty public constructor
