package com.psa.kiosk.gateways

import com.psa.kiosk.entities.Channel
import io.reactivex.Observable

/**
 * Loads the content of a RSS channel from the feed.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
interface ChannelsGateway {
    fun loadChannel(feedUrl: String) : Observable<Channel>
}