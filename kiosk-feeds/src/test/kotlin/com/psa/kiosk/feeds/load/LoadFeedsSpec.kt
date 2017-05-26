package com.psa.kiosk.feeds.load

import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.psa.kiosk.KioskContext.channelsGateway
import com.psa.kiosk.entities.Channel
import com.psa.kiosk.entities.Enclosure
import com.psa.kiosk.entities.Item
import com.psa.kiosk.exceptions.FeedLoadException
import io.kotlintest.mock.mock
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.Observable

class LoadFeedsSpec : BehaviorSpec() {
    init {
        channelsGateway = mock()
        val interactor: LoadFeedInput = LoadFeed()
        val output: LoadFeedOutput = mock()

        val exceptionTable = table(
                headers("url"),
                row("http://wrongurl.com"),
                row("http://www.abc.de"))

        forAll(exceptionTable) { url ->
            Given("there was an error typing the feed with URL $url") {
                val exception = Exception()
                whenever(channelsGateway.loadChannel(url)).thenReturn(Observable.error(exception))

                When("I want to load the feed") {
                    interactor.execute(LoadFeedRequest(url), output)

                    Then("An error response is returned") {
                        verify(channelsGateway).loadChannel(url)
                        verify(output).generateViewModel(argThat<FeedLoadException> {
                            this.url == url && this.cause == exception
                        })
                    }
                }
            }
        }

        val channel1 = Channel("Lone Channel", "http://www.lonechannel.com", "description", "http://lonechannel.com", emptyList())
        val channel2 = Channel(
                "Another Channel",
                "http://www.anotherchannel.com",
                "Another channel for testing",
                "http://www.anotherchannel.com/image.jpg",
                listOf(
                        Item(
                                "Episode 1",
                                "",
                                "Introduction",
                                Enclosure(
                                        "http://www.anotherchannel.com/episode1.mp3",
                                        100,
                                        "audio/mp3"
                                ),
                                0
                        )
                )
        )
        val channel3 = Channel(
                "Test channel",
                "http://www.test.com",
                "A testing channel",
                "",
                emptyList()
        )
        val channel4 = Channel(
                "Final channel",
                "http://www.final.com",
                "Some description",
                "",
                emptyList()
        )
        val successfulRequestsTable = table(
                headers("url", "channels", "response"),
                row("http://empty.feed.com", Observable.empty<Channel>(), LoadFeedResponse()),
                row("http://only.feed.com",
                        Observable.fromArray(channel1),
                        LoadFeedResponse(listOf(channel1))),
                row(
                        "http//rich.feed.com",
                        Observable.fromArray(channel2, channel3, channel4),
                        LoadFeedResponse(listOf(channel2, channel3, channel4))
                )
        )

        forAll(successfulRequestsTable) {url, channels, response ->
            Given("I typed the URL $url") {
                whenever(channelsGateway.loadChannel(url)).thenReturn(channels)

                When("I request the channels for the feed") {
                    interactor.execute(LoadFeedRequest(url), output)

                    Then("I get a response with the expected channels") {
                        verify(channelsGateway).loadChannel(url)
                        verify(output).generateViewModel(response)
                    }
                }
            }
        }
    }
}