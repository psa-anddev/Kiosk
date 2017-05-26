package com.psa.kiosk.feeds.load

import com.psa.kiosk.entities.Channel

/**
 * This is the response class for the load feed use case.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
data class LoadFeedResponse(val channels: List<Channel> = emptyList())