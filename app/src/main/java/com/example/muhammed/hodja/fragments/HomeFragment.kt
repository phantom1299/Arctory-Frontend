package com.example.muhammed.hodja.fragments


import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.muhammed.hodja.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    var map: GoogleMap? = null

    val MAX_DISTANCE = 2500.0
    val MAX_FEE = 2000.0
    val STUDENT_LOCATION = doubleArrayOf(41.080930, 29.030098)
    val LANGUAGE_PREF = "tr"
    var currentMaxDistance: Double = MAX_DISTANCE
    var currentMaxFee: Double = MAX_FEE

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        val filterButton = v.findViewById<FloatingActionButton>(R.id.filterFabButton)
        val myLocationButton = v.findViewById<FloatingActionButton>(R.id.showMyLocation)

        filterButton.setOnClickListener {
            showFilterAlertDialog()
        }

        myLocationButton.setOnClickListener {
            map!!.animateCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(
                                    LatLng(
                                            STUDENT_LOCATION.get(0),
                                            STUDENT_LOCATION.get(1)
                                    ),
                                    15.0f
                            )
            )
        }

        return v
    }

    fun showFilterAlertDialog() {
        val alert = configureFilterAlertDialog()

        alert.show()
    }

    fun configureFilterAlertDialog(): AlertDialog {
        val alert = AlertDialog.Builder(this@HomeFragment.context).create()

        //alert.setTitle("Filtrele")

        val linear = LinearLayout(this.context)

        linear.orientation = LinearLayout.VERTICAL

        val titleTextView = TextView(this.context)

        val maxFeeSeekBarTextView = TextView(this.context)
        maxFeeSeekBarTextView.setPadding(0, 0, 0, 50)
        val maxFeeSeekBar = SeekBar(this.context)
        val maxFeeSeekBarValueTextView = TextView(this.context)
        maxFeeSeekBar.progress = Math.round(currentMaxFee / MAX_FEE * 100).toInt()

        val maxDistanceSeekBarTextView = TextView(this.context)
        maxDistanceSeekBarTextView.setPadding(0, 0, 0, 50)
        val maxDistanceSeekBar = SeekBar(this.context)
        val maxDistanceSeekBarValueTextView = TextView(this.context)

        maxDistanceSeekBarValueTextView.gravity = Gravity.CENTER
        maxDistanceSeekBarValueTextView.textSize = 20.0f
        maxDistanceSeekBarValueTextView.text = currentMaxDistance.toString() + " m"
        maxDistanceSeekBarValueTextView.setPadding(0, 0, 0, 50)

        maxFeeSeekBarValueTextView.gravity = Gravity.CENTER
        maxFeeSeekBarValueTextView.textSize = 20.0f
        maxFeeSeekBarValueTextView.text = currentMaxFee.toString() + " ₺"
        maxFeeSeekBarValueTextView.setPadding(0, 0, 0, 50)

        maxDistanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxDistanceSeekBarValueTextView.text = (p1 * (MAX_DISTANCE / maxDistanceSeekBar.max)).toInt().toString() + """ m"""
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        maxFeeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxFeeSeekBarValueTextView.text = (p1 * (MAX_FEE / maxDistanceSeekBar.max)).toInt().toString() + " ₺"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        maxDistanceSeekBar.progress = Math.round(currentMaxDistance / MAX_DISTANCE * 100).toInt()

        val genderPrefTextView = TextView(this.context)
        val genderSpinner = Spinner(this.context)

        val languagePrefTextView = TextView(this.context)
        val languageSpinner = Spinner(this.context)

        val meetingPointPrefTextView = TextView(this.context)
        val meetingPointSpinner = Spinner(this.context)

        configureFilterAlertDialogPrefTextView(titleTextView, "Filreler", true)
        configureFilterAlertDialogPrefTextView(maxFeeSeekBarTextView, "Maksimum Ücret")
        configureFilterAlertDialogPrefTextView(maxDistanceSeekBarTextView, "Maksimum Mesafe")
        configureFilterAlertDialogPrefTextView(genderPrefTextView, "Cinsiyet")
        configureFilterAlertDialogPrefTextView(languagePrefTextView, "Dil")
        configureFilterAlertDialogPrefTextView(meetingPointPrefTextView, "Buluşma Noktası")

        val genders = ArrayList<String>()
        val languages = ArrayList<String>()
        val meetingPoints = ArrayList<String>()

        genders.add("Farketmez")
        genders.add("Erkek")
        genders.add("Kadın")

        languages.add("Farketmez")
        languages.add("Türkçe")
        languages.add("İngilizce")

        meetingPoints.add("Farketmez")
        meetingPoints.add("Öğretmen")
        meetingPoints.add("Öğrenci")

        genderSpinner.adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_dropdown_item, genders)
        languageSpinner.adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_dropdown_item, languages)
        meetingPointSpinner.adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_dropdown_item, meetingPoints)

        linear.addView(titleTextView)
        linear.addView(maxFeeSeekBarTextView)
        linear.addView(maxFeeSeekBar)
        linear.addView(maxFeeSeekBarValueTextView)
        linear.addView(maxDistanceSeekBarTextView)
        linear.addView(maxDistanceSeekBar)
        linear.addView(maxDistanceSeekBarValueTextView)
        linear.addView(genderPrefTextView)
        linear.addView(genderSpinner)
        linear.addView(languagePrefTextView)
        linear.addView(languageSpinner)
        linear.addView(meetingPointPrefTextView)
        linear.addView(meetingPointSpinner)

        alert.setView(linear)

        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Filtrele", { dialogInterface, i ->

            // Calculate max fee and max distance
            val maxFee = maxFeeSeekBar.progress * (MAX_FEE / maxFeeSeekBar.max)
            val maxDistance = maxDistanceSeekBar.progress * (MAX_DISTANCE / maxDistanceSeekBar.max)
            currentMaxFee = maxFee
            currentMaxDistance = maxDistance
            getData(maxFee, maxDistance)
        })

        return alert
    }

    fun configureFilterAlertDialogPrefTextView(textView: TextView, value: String, title: Boolean = false) {
        textView.text = value

        if (title) {
            textView.textSize = 22.0f
            textView.setPadding(15, 10, 10, 25)
        } else {
            textView.textSize = 20.0f
            textView.setPadding(15, 10, 10, 10)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val pp = LatLng(STUDENT_LOCATION.get(0), STUDENT_LOCATION.get(1))

        val option = MarkerOptions()

        option
                .position(pp)
                .title("Başlangıç")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.student_map_icon, 200, 200)))

        map!!.addMarker(option)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(pp))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(pp, 15.0f))

        getData()
    }

    fun getBitmapResized(resourceId: Int, width: Int, height: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

        return resizedBitmap
    }

    fun getData(
            maxFee: Double = MAX_FEE,
            maxDistance: Double = MAX_DISTANCE,
            genderPref: String = "[\"Male\", \"Female\"]",
            meetingPoints: String = "[\"student\", \"teacher\"]",
            language: String = LANGUAGE_PREF,
            studentLocation: String = "[${STUDENT_LOCATION.get(0)}, ${STUDENT_LOCATION.get(1)}]"
    ) {
        Fuel.get(
                getString(R.string.lessonsUrl),
                listOf(
                        "maxFee" to maxFee,
                        "maxDistance" to maxDistance,
                        "studentLocation" to studentLocation,
                        "gender" to genderPref,
                        "meetingPoints" to meetingPoints,
                        "language" to language
                )
        ).responseJson { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                }
                is Result.Success -> {
                    val resultObj = result.value.obj() //JSONObj
                    registerMarkers(resultObj.getJSONArray("result"))
                }
            }
        }
    }

    private fun registerMarkers(jsonArray: JSONArray) {
        clearMap()

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

            option
                    .position(location)
                    .title(teacherName)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.teacher_map_icon, 200, 200)))
                    .snippet(lessonName.toString())

            map!!.addMarker(option)
        }
    }

    private fun clearMap() {
        map!!.clear()
        val markerOptions = MarkerOptions()

        val circleOptions = CircleOptions()
        circleOptions
                .center(LatLng(STUDENT_LOCATION.get(0), STUDENT_LOCATION.get(1)))
                .radius(currentMaxDistance)
                .fillColor(Color.argb(125, 255, 245, 56))
                .strokeColor(Color.argb(40, 255, 245, 56))
        map!!.addCircle(circleOptions)

        markerOptions
                .position(LatLng(STUDENT_LOCATION.get(0), STUDENT_LOCATION.get(1)))
                .title("Başlangıç")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.student_map_icon, 200, 200)))

        map!!.addMarker(markerOptions)
    }

}// Required empty public constructor
