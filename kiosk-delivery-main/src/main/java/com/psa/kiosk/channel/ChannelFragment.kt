package com.psa.kiosk.channel

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.psa.kiosk.R
import com.psa.kiosk.snack
import kotlinx.android.synthetic.main.fragment_channel.*

/**
 * A fragment to display a feed. There's a spinner on top to change the channel and a recycler view
 * to display the items.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelFragment : LifecycleFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProviders.of(activity)[ChannelsViewModel::class.java].channelsData.observe(this,
                Observer {
                    when(it) {
                        is ChannelListData -> channels.adapter = ChannelsAdapter(it.channels)
                        is MessageData -> view?.snack(getString(it.message, it.url), Snackbar.LENGTH_LONG)
                    }
                })
        items.layoutManager = LinearLayoutManager(activity)
        items.adapter = ItemsAdapter()
        channels.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                (items.adapter as ItemsAdapter).items = emptyList()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (items.adapter as ItemsAdapter).items = (channels.selectedItem as ChannelFeedData).items
            }

        }
    }
}
