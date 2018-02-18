package com.example.muhammed.hodja

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammed.hodja.objects.UserDetail


/**
 * Created by ogrenci on 18.02.2018.
 */
class UserDetailsAdapter(
        private var activity: Activity,
        private var userDetails: ArrayList<UserDetail>
) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var nameLabel: TextView? = null
        var detailLabel: TextView? = null
        var imageView: ImageView? = null

        init {
            nameLabel = row?.findViewById(R.id.nameLabel)
            detailLabel = row?.findViewById(R.id.detailLabel)
            imageView = row?.findViewById(R.id.profilePhoto)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.list_item_user_details, null)

            viewHolder = ViewHolder(view)

            view.tag = viewHolder
        } else {
            view = convertView

            viewHolder = view?.tag as ViewHolder
        }

        val detail = userDetails[position]

        viewHolder.nameLabel?.text = detail.name
        viewHolder.detailLabel?.text = detail.startDate + "-" + detail.finishDate

        // ImageView ı burada setle, fotoğrafı internetten indirmek gerek

        return view as View
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return userDetails[position]
    }

    override fun getCount(): Int {
        return userDetails.size
    }

}