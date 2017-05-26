package com.psa.kiosk

import android.app.Application
import com.psa.kiosk.gateways.RetrofitChannelsGateway
import net.danlew.android.joda.JodaTimeAndroid

/**
 * This application class starts Joda time for Android and sets up the gateway implementations.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class KioskApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        KioskContext.channelsGateway = RetrofitChannelsGateway()
    }
}