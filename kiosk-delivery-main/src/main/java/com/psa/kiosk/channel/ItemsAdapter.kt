package com.psa.kiosk.channel

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.psa.kiosk.R
import kotlinx.android.synthetic.main.item_feed_item.view.*

/**
 * An adapter to render the items of each channel.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ItemsAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var items: List<ItemData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(viewHolder: ItemViewHolder?, position: Int) {
        viewHolder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_feed_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int =
            items.size

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(itemData: ItemData) {
        itemView.title.text = itemData.title
        itemView.description.text = itemData.description

        if (itemData.link == "")
            itemView.link.visibility = View.GONE
        else {
            itemView.link.visibility = View.VISIBLE
            itemView.link.setOnClickListener {
                it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(itemData.link)))
            }
        }

        if (itemData.mediaUrl == "")
            itemView.play.visibility = View.GONE
        else {
            itemView.play.visibility = View.VISIBLE
            itemView.play.setOnClickListener {
                it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(itemData.mediaUrl)))
            }
        }
    }
}