package com.example.muhammed.hodja.objects

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammed.hodja.R
import java.util.*

class UserDetailsAdapter(
        private var activity: Activity,
        private var userDetails: ArrayList<UserDetail>?
) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var nameLabel: TextView? = null
        var detailLabel: TextView? = null
        var imageView: ImageView? = null

        init {
            nameLabel = row?.findViewById(R.id.schoolNameLabel)
            detailLabel = row?.findViewById(R.id.detailLabel)
            imageView = row?.findViewById(R.id.profilePhoto)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.list_item_user_details, null)

            viewHolder = ViewHolder(view)

            view.tag = viewHolder
        } else {
            view = convertView

            viewHolder = view.tag as ViewHolder
        }

        val detail = userDetails?.get(position)

        viewHolder.nameLabel?.text = detail?.name

        var detailLabel = ""
        if (detail?.startDate != null) {
            detailLabel = detail.startDate!!.substring(0, Math.min(detail.startDate!!.length, 4))
        }

        if (detail?.finishDate != null) {
            detailLabel += " - " + detail.startDate!!.substring(0, Math.min(detail.finishDate!!.length, 4))
        }
        viewHolder.detailLabel?.text = detailLabel

        // ImageView ı burada setle, fotoğrafı internetten indirmek gerek

        return view as View
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        if (userDetails != null)
            return userDetails!![position]

        return ArrayList<UserDetail>()
    }

    override fun getCount(): Int {
        if (userDetails != null)
            return userDetails!!.size

        return 0
    }

}