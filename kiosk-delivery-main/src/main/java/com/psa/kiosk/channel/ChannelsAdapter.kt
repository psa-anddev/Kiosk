package com.psa.kiosk.channel

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import com.psa.kiosk.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_channel.view.*
import kotlinx.android.synthetic.main.item_channel_list.view.*

/**
 * Adapter for the channels spinner.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelsAdapter(private val channels : List<ChannelFeedData>) : SpinnerAdapter {
    private val imageSize = 150
    override fun isEmpty(): Boolean = channels.isEmpty()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_channel, parent, false)
        view.title.text = channels[position].title
        view.description.text = channels[position].description
        val image = channels[position].image
        if (image == "")
            view.channelImage.visibility = View.GONE
        else {
            view.channelImage.visibility = View.VISIBLE
            Picasso.with(view.context)
                    .load(image)
                    .resize(imageSize, imageSize)
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(view.channelImage)
        }
        return view
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getItemViewType(position: Int): Int = 0

    override fun getItem(position: Int): ChannelFeedData = channels[position]



    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_channel_list, parent, false)
        view.listTitle.text = channels[position].title
        if (channels[position].image == "")
            view.listChannelImage.visibility = View.GONE
        else {
            view.listChannelImage.visibility = View.VISIBLE
            Picasso.with(view.context)
                    .load(channels[position].image)
                    .resize(imageSize, imageSize)
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(view.listChannelImage)
        }
        return view
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int = channels.size
}