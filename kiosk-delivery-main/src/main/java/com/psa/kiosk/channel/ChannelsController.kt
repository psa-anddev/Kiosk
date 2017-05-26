package com.psa.kiosk.channel

import com.psa.kiosk.feeds.load.LoadFeed
import com.psa.kiosk.feeds.load.LoadFeedInput

/**
 * This class controls the execution of action related to channels.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelsController {
    fun loadChannels() : LoadFeedInput =
            LoadFeed()
}