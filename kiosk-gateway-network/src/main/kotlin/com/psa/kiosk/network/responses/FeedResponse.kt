package com.psa.kiosk.network.responses

import com.psa.kiosk.network.models.ChannelModel
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Response returned by the RSS service when a feed is
 * requested.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
@Root
data class FeedResponse(
    @field:ElementList(entry = "channel", inline = true, required = false) var channels : List<ChannelModel> = mutableListOf()
)
