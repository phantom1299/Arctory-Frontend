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


/**
 * A simple [Fragment] subclass.
 */
class StudentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_student, container, false)

        var adapter = UserAdapter(activity, generateData())
        var listView = v.findViewById<ListView>(R.id.userListView)

        listView.adapter = adapter

        adapter.notifyDataSetChanged()

        // Gridview

        var gridView = v.findViewById<GridView>(R.id.applicationGridView)
        var adapter2 = UserGridViewAdapter(activity, generateData())

        gridView.adapter = adapter2

        adapter2.notifyDataSetChanged()
        // Inflate the layout for this fragment
        return v
    }

    fun generateData(): ArrayList<User> {
        var temp = ArrayList<User>()

        temp.add(User("1234", "Emircan Kavas", ""))
        temp.add(User("1234", "Bahaeddin Aydemir", ""))
        temp.add(User("1234", "Bahaeddin Aydemir", ""))
        temp.add(User("1234", "Bahaeddin Aydemir", ""))
        temp.add(User("1234", "Bahaeddin Aydemir", ""))
        temp.add(User("1234", "Bahaeddin Aydemir", ""))

        return temp
    }

}// Required empty public constructor
