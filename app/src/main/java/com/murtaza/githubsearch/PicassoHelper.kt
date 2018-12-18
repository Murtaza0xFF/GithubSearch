package com.murtaza.githubsearch

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class PicassoHelper {
    fun setImageView(placeHolder: Int, imageUrl: String, imageView: ImageView){
        Picasso
            .get()
            .load(imageUrl)
            .error(placeHolder)
            .placeholder(placeHolder)
            .into(imageView)
    }
}
