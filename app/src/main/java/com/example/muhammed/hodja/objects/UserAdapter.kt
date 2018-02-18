package com.example.muhammed.hodja

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammed.hodja.objects.User


/**
 * Created by ogrenci on 18.02.2018.
 */
class UserAdapter(
        private var activity: Activity,
        private var users: ArrayList<User>
) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var nameLabel: TextView? = null
        var imageView: ImageView? = null

        init {
            nameLabel = row?.findViewById(R.id.nameLabel)
            imageView = row?.findViewById(R.id.profilePhoto)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.user_row, null)

            viewHolder = ViewHolder(view)

            view.tag = viewHolder
        } else {
            view = convertView

            viewHolder = view?.tag as ViewHolder
        }

        val user = users[position]

        viewHolder.nameLabel?.text = user.name

        // ImageView ı burada setle, fotoğrafı internetten indirmek gerek

        return view as View
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getCount(): Int {
        return users.size
    }

}