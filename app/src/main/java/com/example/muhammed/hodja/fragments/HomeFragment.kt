package com.example.muhammed.hodja.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.muhammed.hodja.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home.*


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
        val pp = LatLng(40.0, 28.0)
        val option = MarkerOptions()
        option.position(pp).title("Başlangıç")
        map!!.addMarker(option)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(pp))
        Log.d("Result", "Here")
        getData()
    }

    fun getData() {
        Fuel.get(getString(R.string.lessonsUrl), listOf("maxFee" to "1000", "maxDistance" to "500", "studentLocation" to "[50,33]")).responseString { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    textView3.text = result.toString()
                }
                is Result.Success -> {
                    textView3.text = result.toString()
                }
            }
        }
    }

}// Required empty public constructor
