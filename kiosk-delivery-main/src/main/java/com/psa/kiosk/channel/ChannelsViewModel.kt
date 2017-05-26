package com.psa.kiosk.channel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.psa.kiosk.R
import com.psa.kiosk.entities.Channel
import com.psa.kiosk.entities.Item
import com.psa.kiosk.exceptions.FeedLoadException
import com.psa.kiosk.feeds.load.LoadFeedOutput
import com.psa.kiosk.feeds.load.LoadFeedRequest
import com.psa.kiosk.feeds.load.LoadFeedResponse
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.joda.time.format.DateTimeFormat

/**
 * View model for the channels.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelsViewModel(channelsData: MutableLiveData<ChannelsData> = MutableLiveData()) : ViewModel(), LoadFeedOutput {
    val channelsData: LiveData<ChannelsData> = channelsData
    private val controller = ChannelsController()

    override fun generateViewModel(response: LoadFeedResponse) {
        Completable.fromAction {
            (channelsData as MutableLiveData).value =
                    prepareResponseForPrinting(response)
        }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


    override fun generateViewModel(exception: FeedLoadException) {
        Completable.fromAction {
            (channelsData as MutableLiveData).value = MessageData(R.string.load_feed_error, exception.url)
        }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun loadFeed(url : String) {
        controller.loadChannels().execute(LoadFeedRequest(url), this)
    }

    private fun prepareResponseForPrinting(response: LoadFeedResponse): ChannelListData =
            ChannelListData(
                    response.channels
                            .map { prepareChannelForPrinting(it) }
            )

    private fun prepareChannelForPrinting(channel: Channel): ChannelFeedData =
            ChannelFeedData(
                    channel.title,
                    channel.link,
                    channel.description,
                    channel.image,
                    channel.items.map { prepareItemForPrinting(it) }
            )


    private fun prepareItemForPrinting(item: Item): ItemData =
            ItemData(
                    item.title,
                    item.link,
                    item.description,
                    item.enclosure.url,
                    DateTimeFormat.mediumDateTime().print(item.pubDate)
            )
}