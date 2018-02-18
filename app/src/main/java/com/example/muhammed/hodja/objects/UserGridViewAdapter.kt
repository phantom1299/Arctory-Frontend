package com.example.muhammed.hodja

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammed.hodja.objects.User

class UserGridViewAdapter(
        private var activity: Activity,
        private var applications: ArrayList<User>
        private var applicationIds: ArrayList<String>
) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var nameLabel: TextView? = null
        var imageView: ImageView? = null
        var acceptButton: Button? = null
        var rejectButton: Button? = null

        init {
            nameLabel = row?.findViewById(R.id.nameLabelGrid)
            imageView = row?.findViewById(R.id.profilePhotoGrid)
            acceptButton = row?.findViewById(R.id.acceptButtonGrid)
            rejectButton = row?.findViewById(R.id.rejectButtonGrid)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.user_grid_cell, null)

            viewHolder = ViewHolder(view)

            view.tag = viewHolder
        } else {
            view = convertView

            viewHolder = view?.tag as ViewHolder
        }

        val application = applications[position]
        val applicationId = applicationIds[position]

        viewHolder.nameLabel?.text = "Başvuran Adı"

        // ImageView ı burada setle, fotoğrafı internetten indirmek gerek

        viewHolder.acceptButton?.setOnClickListener {
            // Application kabul etme isteğini burada gönder

        }

        viewHolder.rejectButton?.setOnClickListener {
            // Application reddetme isteğini burada gönder
        }

        return view as View
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return applications[position]
    }

    override fun getCount(): Int {
        return applications.size
    }

}