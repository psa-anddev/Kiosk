package com.psa.kiosk.channel

import android.support.annotation.StringRes

/**
 * Class that holds printable data for channels.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
sealed class ChannelsData

data class MessageData(
        @StringRes val message : Int,
        val url : String
) : ChannelsData()

data class ChannelListData(val channels : List<ChannelFeedData> = emptyList()) : ChannelsData()

data class ChannelFeedData(
        val title : String,
        val link : String,
        val description : String,
        val image : String,
        val items : List<ItemData> = emptyList()
)

data class ItemData(
        val title : String,
        val link: String,
        val description: String,
        val mediaUrl : String,
        val pubDate : String
)
