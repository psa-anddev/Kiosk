package com.psa.kiosk.channel

import com.psa.kiosk.feeds.load.LoadFeed
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Tests for the channels controller.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelsControllerSpec : StringSpec({
    val controller = ChannelsController()

    "should return the right interactor" {
        (controller.loadChannels() is LoadFeed) shouldBe true
    }

})
