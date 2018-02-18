package com.example.muhammed.hodja.fragments


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import com.example.muhammed.hodja.R
import com.example.muhammed.hodja.UserDetailsActivity
import com.example.muhammed.hodja.objects.Lesson
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


class HomeFragment : Fragment(), OnMapReadyCallback {

    var map: GoogleMap? = null
    var lessons: ArrayList<Lesson> = ArrayList<Lesson>()

    val MAX_DISTANCE = 2500.0
    val MAX_FEE = 2000.0
    val STUDENT_LOCATION = doubleArrayOf(41.080930, 29.030098)
    val LANGUAGE_PREF = "tr"
    var currentMaxDistance: Double = MAX_DISTANCE
    var currentMaxFee: Double = MAX_FEE
    lateinit var v: View

    var genderSpinnerPosition: Int = 0
    var languageSpinnerPosition: Int = 0
    var meetingPointSpinnerPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater!!.inflate(R.layout.fragment_home, container, false)


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
                                    14.0f
                            )
            )
        }

        return v
    }

    fun showFilterAlertDialog() {
        val dialogView = LayoutInflater.from(this@HomeFragment.context).inflate(R.layout.lesson_filter_dialog, null)

        val alert = AlertDialog.Builder(this@HomeFragment.context).create()

        val maxFeeValueLabel = dialogView.findViewById<TextView>(R.id.maxFeeValueLabel)
        maxFeeValueLabel.text = "$currentMaxFee ₺"

        val maxDistanceValueLabel = dialogView.findViewById<TextView>(R.id.maxDistanceValueLabel)
        maxDistanceValueLabel.text = "$currentMaxDistance ₺"

        val maxFeeSeekBar = dialogView.findViewById<SeekBar>(R.id.maxFeeSeekBar)
        maxFeeSeekBar.progress = Math.round(currentMaxFee / MAX_FEE * 100).toInt()

        val maxDistanceSeekBar = dialogView.findViewById<SeekBar>(R.id.maxDistanceSeekBar)
        maxDistanceSeekBar.progress = Math.round(currentMaxDistance / MAX_DISTANCE * 100).toInt()

        maxFeeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxFeeValueLabel.text = (p1 * (MAX_DISTANCE / maxFeeSeekBar.max)).toInt().toString() + " ₺"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        maxDistanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxDistanceValueLabel.text = (p1 * (MAX_DISTANCE / maxDistanceSeekBar.max)).toInt().toString() + " m"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        val genderSpinner = dialogView.findViewById<Spinner>(R.id.genderSpinner)
        val languageSpinner = dialogView.findViewById<Spinner>(R.id.languageSpinner)
        val meetingPointSpinner = dialogView.findViewById<Spinner>(R.id.meetingPointSpinner)

        genderSpinner.setSelection(genderSpinnerPosition)
        languageSpinner.setSelection(languageSpinnerPosition)
        meetingPointSpinner.setSelection(meetingPointSpinnerPosition)

        dialogView.findViewById<Button>(R.id.filterButton).setOnClickListener {
            alert.dismiss()
            val maxFee = maxFeeSeekBar.progress * (MAX_FEE / maxFeeSeekBar.max)
            val maxDistance = maxDistanceSeekBar.progress * (MAX_DISTANCE / maxDistanceSeekBar.max)
            currentMaxFee = maxFee
            currentMaxDistance = maxDistance

            // Set gender pref for URL by selection
            var genders = ""
            genderSpinnerPosition = genderSpinner.selectedItemPosition
            when (genderSpinnerPosition) {
                0 -> genders = "[\"Male\", \"Female\"]"
                1 -> genders = "[\"Male\"]"
                2 -> genders = "[\"Female\"]"
            }

            // Set language pref for url

            languageSpinnerPosition = languageSpinner.selectedItemPosition
            val language = getLanguageCodeByPosition(languageSpinnerPosition)

            // Set meetingPoints pref for URL query param
            var meetingPoints = ""
            meetingPointSpinnerPosition = meetingPointSpinner.selectedItemPosition
            when (meetingPointSpinnerPosition) {
                0 -> meetingPoints = "[\"student\", \"teacher\"]"
                1 -> meetingPoints = "[\"teacher\"]"
                2 -> meetingPoints = "[\"student\"]"
            }

            getData(maxFee, maxDistance, genders, meetingPoints, language)
        }

        alert.setView(dialogView)
        alert.show()
    }

    fun getLanguageCodeByPosition(position: Int): String {
        var code = ""

        when (position) {
            0 -> code = "tr"
            1 -> code = "en"
            2 -> code = "de"
        }

        return code
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

        map!!.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this@HomeFragment.context, UserDetailsActivity::class.java)

            intent.putExtra("_id", marker.tag as String)

            startActivity(intent)
        }

        val pp = LatLng(STUDENT_LOCATION.get(0), STUDENT_LOCATION.get(1))

        val option = MarkerOptions()

        option
                .position(pp)
                .title("Başlangıç")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.student_map_icon, 200, 200)))

        map!!.addMarker(option)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(pp))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(pp, 14.0f))

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

        lessons.clear()

        for (i in 0..jsonArray.length() - 1) {
            val lesson = Lesson(jsonArray[i] as JSONObject)
            lessons.add(lesson)

            option = MarkerOptions()

            option
                    .position(lesson.teacher!!.location!!)
                    .title(lesson.teacher!!.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.teacher_map_icon, 200, 200)))
                    .snippet(lesson.name)

            val marker = map!!.addMarker(option)

            marker.tag = lesson.teacher!!._id
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
                .title("Siz")
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapResized(R.drawable.student_map_icon, 200, 200)))

        map!!.addMarker(markerOptions)
    }

}// Required empty public constructor
