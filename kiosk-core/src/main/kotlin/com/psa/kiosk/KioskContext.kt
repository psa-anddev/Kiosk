package com.psa.kiosk

import com.psa.kiosk.gateways.ChannelsGateway

/**
 * This class contains all the wiring configuration for the application.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
object KioskContext {
    lateinit var channelsGateway : ChannelsGateway
}