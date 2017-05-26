package com.psa.kiosk.channel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.verify
import com.psa.kiosk.R
import com.psa.kiosk.entities.Channel
import com.psa.kiosk.entities.Enclosure
import com.psa.kiosk.entities.Item
import com.psa.kiosk.exceptions.FeedLoadException
import com.psa.kiosk.feeds.load.LoadFeedOutput
import com.psa.kiosk.feeds.load.LoadFeedResponse
import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.shouldBe
import io.kotlintest.mock.mock
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.StringSpec
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.joda.time.format.DateTimeFormat

@Suppress("IllegalIdentifier")
/**
 * Tests of the channels view model.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class ChannelsViewModelSpec : StringSpec() {
    init {
        val channelsData: MutableLiveData<ChannelsData> = mock()
        val viewModel = ChannelsViewModel(channelsData)
        val failsTable = table(
                headers("url"),
                row("http://feed.com"),
                row("http://feed.podcast.com")
        )
        val successfulTable = table(
                headers("url", "response", "data"),
                row(
                        "http://emptyfeed.com",
                        LoadFeedResponse(),
                        ChannelListData()
                ),
                row(
                        "http://lost.robin.song",
                        `one channel with no episode feed response`(),
                        `one channel with no episode data`()
                ),
                row(
                        "http://android.tes.com",
                        `one channel with one episode response`(),
                        `one channel with one episode data`()
                ),
                row(
                        "http://clojure-hickey.com",
                        `one channel with three episodes response`(),
                        `one channel with three episodes data`()

                ),
                row(
                        "http://multichannelfeed.com",
                        `multichannel feed response`(),
                        `multichannel feed data`()
                )
        )



        "should be an Android view model" {
            (viewModel is ViewModel) shouldBe true
        }

        "should implement the load feeds output boundary" {
            (viewModel is LoadFeedOutput) shouldBe true
        }

        forAll(failsTable) {
            "should return error data when an exception occurs for $it" {
                viewModel.generateViewModel(FeedLoadException(it, Throwable()))
                verify(channelsData).setValue(MessageData(R.string.load_feed_error, it))
            }
        }

        forAll(successfulTable) {
            url, response, data ->
            "should return the right data for the channels in $url" {
                viewModel.generateViewModel(response)
                verify(channelsData).setValue(data)
            }
        }
    }

    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        test()
        RxAndroidPlugins.reset()
    }

    private fun `multichannel feed data`(): ChannelListData {
        return ChannelListData(
                listOf(
                        ChannelFeedData(
                                "Channel 1",
                                "http://multichannelfeed.com/channel1",
                                "First Channel",
                                "http://multichannelfeed.com/channel1/logo.png",
                                listOf(
                                        ItemData(
                                                "First interesting piece of news",
                                                "http://multichannelfeed.com/channel1/first.php",
                                                "First interesting piece of news revealed",
                                                "",
                                                DateTimeFormat.mediumDateTime().print(1493289168000)
                                        )
                                )
                        ),
                        ChannelFeedData(
                                "Channel 2",
                                "http://multichannelfeed.com/channel2",
                                "Second Channel",
                                "http://multichannelfeed.com/channel2/logo.png",
                                listOf(
                                        ItemData(
                                                "Some piece of news",
                                                "http://multichannelfeed.com/channel2/news.php",
                                                "Some interesting piece of news",
                                                "",
                                                DateTimeFormat.mediumDateTime().print(1492684368000)
                                        )
                                )
                        ),
                        ChannelFeedData(
                                "Channel 3",
                                "http://multichannelfeed.com/channel3",
                                "The third channel",
                                "http://mutichannelfeed.com/channel3/logo.png",
                                listOf(
                                        ItemData(
                                                "Test article",
                                                "http://multichannelfeed.com/channel3/test.php",
                                                "This is a test article.",
                                                "",
                                                DateTimeFormat.mediumDateTime().print(1492857168000)
                                        )
                                )
                        )
                )
        )
    }

    private fun `multichannel feed response`(): LoadFeedResponse {
        return LoadFeedResponse(
                listOf(
                        Channel(
                                "Channel 1",
                                "http://multichannelfeed.com/channel1",
                                "First Channel",
                                "http://multichannelfeed.com/channel1/logo.png",
                                listOf(
                                        Item(
                                                "First interesting piece of news",
                                                "http://multichannelfeed.com/channel1/first.php",
                                                "First interesting piece of news revealed",
                                                Enclosure("", 0, ""),
                                                1493289168000
                                        )
                                )
                        ),
                        Channel(
                                "Channel 2",
                                "http://multichannelfeed.com/channel2",
                                "Second Channel",
                                "http://multichannelfeed.com/channel2/logo.png",
                                listOf(
                                        Item(
                                                "Some piece of news",
                                                "http://multichannelfeed.com/channel2/news.php",
                                                "Some interesting piece of news",
                                                Enclosure("", 0, ""),
                                                1492684368000
                                        )
                                )
                        ),
                        Channel(
                                "Channel 3",
                                "http://multichannelfeed.com/channel3",
                                "The third channel",
                                "http://mutichannelfeed.com/channel3/logo.png",
                                listOf(
                                        Item(
                                                "Test article",
                                                "http://multichannelfeed.com/channel3/test.php",
                                                "This is a test article.",
                                                Enclosure("", 0, ""),
                                                1492857168000
                                        )
                                )
                        )
                )
        )
    }

    private fun `one channel with three episodes data`(): ChannelListData {
        return ChannelListData(
                listOf(
                        ChannelFeedData(
                                "Clojure Hickey podcast",
                                "http://clojure-hickey.com",
                                "A podcast about Clojure",
                                "http://clojure-hickey.com/a.jpg",
                                listOf(
                                        ItemData(
                                                "Persistent data Structures",
                                                "",
                                                "A view of persistent data structures",
                                                "http://clojure-hickey.com/episodes/01.mp3",
                                                DateTimeFormat.mediumDateTime().print(1494412368000)
                                        ),
                                        ItemData(
                                                "Macros",
                                                "",
                                                "Macros in the Clojure language",
                                                "http://clojure-hickey.com/episodes/02.mp3",
                                                DateTimeFormat.mediumDateTime().print(1494844368000)
                                        ),
                                        ItemData(
                                                "Atomic",
                                                "",
                                                "Atomic, a functional database",
                                                "http://clojure-hickey.com/episodes/03.mp3",
                                                DateTimeFormat.mediumDateTime().print(1495276368000)
                                        )
                                )
                        )
                )
        )
    }

    private fun `one channel with three episodes response`(): LoadFeedResponse {
        return LoadFeedResponse(
                listOf(
                        Channel(
                                "Clojure Hickey podcast",
                                "http://clojure-hickey.com",
                                "A podcast about Clojure",
                                "http://clojure-hickey.com/a.jpg",
                                listOf(
                                        Item(
                                                "Persistent data Structures",
                                                "",
                                                "A view of persistent data structures",
                                                Enclosure(
                                                        "http://clojure-hickey.com/episodes/01.mp3",
                                                        100, "audio/mpeg"
                                                ),
                                                1494412368000
                                        ),
                                        Item(
                                                "Macros",
                                                "",
                                                "Macros in the Clojure language",
                                                Enclosure(
                                                        "http://clojure-hickey.com/episodes/02.mp3",
                                                        4096, "audio/mpeg"
                                                ),
                                                1494844368000
                                        ),
                                        Item(
                                                "Atomic",
                                                "",
                                                "Atomic, a functional database",
                                                Enclosure(
                                                        "http://clojure-hickey.com/episodes/03.mp3",
                                                        8192, "audio/mpeg"
                                                ),
                                                1495276368000
                                        )
                                )
                        )
                )
        )
    }

    private fun `one channel with one episode data`(): ChannelListData {
        return ChannelListData(
                listOf(
                        ChannelFeedData(
                                "Android test channel",
                                "http://android.test.org",
                                "An Android test channel",
                                "http://android.test.org/images/logo.jpg",
                                listOf(
                                        ItemData(
                                                "Ep 01 - Espresso",
                                                "http://android.test.org/episodes/01",
                                                "A tour through Espresso",
                                                "",
                                                DateTimeFormat.mediumDateTime().print(1495621968000)
                                        )
                                )
                        )
                )
        )
    }

    private fun `one channel with one episode response`(): LoadFeedResponse {
        return LoadFeedResponse(
                listOf(
                        Channel(
                                "Android test channel",
                                "http://android.test.org",
                                "An Android test channel",
                                "http://android.test.org/images/logo.jpg",
                                listOf(
                                        Item(
                                                "Ep 01 - Espresso",
                                                "http://android.test.org/episodes/01",
                                                "A tour through Espresso",
                                                Enclosure("", 0, ""),
                                                1495621968000
                                        )
                                )
                        )
                )
        )
    }

    private fun `one channel with no episode data`(): ChannelListData {
        return ChannelListData(
                listOf(
                        ChannelFeedData(
                                "Lost (Robin's song)",
                                "http://lost.robin.song",
                                "A channel about Haggard",
                                "http://lost.robin.song/image.png"
                        )
                )
        )
    }

    private fun `one channel with no episode feed response`(): LoadFeedResponse {
        return LoadFeedResponse(
                listOf(
                        Channel(
                                "Lost (Robin's song)",
                                "http://lost.robin.song",
                                "A channel about Haggard",
                                "http://lost.robin.song/image.png",
                                emptyList()
                        )
                )
        )
    }
}