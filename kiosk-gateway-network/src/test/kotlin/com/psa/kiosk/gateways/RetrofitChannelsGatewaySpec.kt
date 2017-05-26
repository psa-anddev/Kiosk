package com.psa.kiosk.gateways

import com.psa.kiosk.entities.Channel
import com.psa.kiosk.entities.Enclosure
import com.psa.kiosk.entities.Item
import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import io.kotlintest.specs.BehaviorSpec
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Specification for the channels gateway which uses Retrofit to gather the channels data.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class RetrofitChannelsGatewaySpec : BehaviorSpec() {
    init {
        val gateway: ChannelsGateway = RetrofitChannelsGateway()
        val errorResponsesTable = table(
                headers("errorCode"),
                row(401),
                row(403)
        )
        val successfulResponsesTable = table(
                headers("description", "response", "expectedChannels"),
                row("an empty feed",
                        """<rss version="2.0" />""",
                        emptyArray<Channel>()),
                row(
                        "a feed with an empty channel",
                        """
                            <rss version="0.9.1">
                                <channel>
                                    <title>WriteTheWeb</title>
                                    <link>http://writetheweb.com</link>
                                    <description>News for the web users that write back</description>
                                </channel>
                            </rss>
                        """,
                        arrayOf(
                                Channel("WriteTheWeb", "http://writetheweb.com",
                                        "News for the web users that write back",
                                        "", emptyList())
                        )
                ),
                row(
                        "a feed with a channel with one item",
                        """
                            <rss version = "2.0">
                                <channel>
                                    <title>One chapter</title>
                                    <link>http://onechannel.org</link>
                                    <description>The channel with one chapter</description>
                                    <image>
                                        <url>http://onechannel.org/image.jpg</url>
                                        <link>http://onechannel.org</link>
                                        <title>One Channel image</title>
                                    </image>
                                    <item>
                                            <title>Episode 1</title>
                                    </item>
                                </channel>
                            </rss>
                        """,
                        arrayOf(
                                Channel("One chapter",
                                        "http://onechannel.org",
                                        "The channel with one chapter",
                                        "http://onechannel.org/image.jpg",
                                        listOf(
                                                Item("Episode 1", "", "", Enclosure("", 0, ""), 0)))
                        )
                ),
                row(
                        "A channel with three items",
                        """
                            <rss version = "1.0">
                                <channel>
                                    <title>Three chapters</title>
                                    <description>A channel with three chapters</description>
                                    <link>http://3chapters.com</link>
                                    <item>
                                        <description>Chapter one is described here</description>
                                        <link>http://3chapters.com/chapter01</link>
                                    </item>
                                    <item>
                                        <title>Chapter two</title>
                                        <enclosure url = "http://3chapters.com/ch02.mp3" length="100" type="audio/mpeg" />
                                        <pubDate>Mon, 15 May 2017 16:26:10 GMT</pubDate>
                                    </item>
                                    <item>
                                        <title>Episode 3</title>
                                        <description>Irrelevant</description>
                                        <pubDate>Tue, 23 May 2017 14:02:14 GMT</pubDate>
                                    </item>
                                </channel>
                            </rss>
                        """,
                        arrayOf(
                                Channel(
                                        "Three chapters",
                                        "http://3chapters.com",
                                        "A channel with three chapters",
                                        "",
                                        listOf(
                                                Item(
                                                        "",
                                                        "http://3chapters.com/chapter01",
                                                        "Chapter one is described here",
                                                        Enclosure("", 0, ""),
                                                        0
                                                ),
                                                Item(
                                                        "Chapter two",
                                                        "",
                                                        "",
                                                        Enclosure("http://3chapters.com/ch02.mp3", 100, "audio/mpeg"),
                                                        1494865570000
                                                ),
                                                Item(
                                                        "Episode 3",
                                                        "",
                                                        "Irrelevant",
                                                        Enclosure("", 0, ""),
                                                        1495548134000
                                                )
                                        ))
                        )
                ),
                row(
                        "three channels with one chapter each",
                        """
                                <rss version = "1.0">
                                    <channel>
                                        <title>Channel 1</title>
                                        <link>http://channel1.com</link>
                                        <description>First channel</description>
                                        <item><title>Ep01</title></item>
                                    </channel>
                                    <channel>
                                        <title>Channel 2</title>
                                        <link>http://channel2.com</link>
                                        <description>Second channel</description>
                                        <item><title>Ep201</title></item>
                                    </channel>
                                    <channel>
                                        <title>Channel 3</title>
                                        <link>http://channel3.com</link>
                                        <description>Third channel</description>
                                        <item><title>Ep301</title></item>
                                    </channel>
                                </rss>
                                """,
                        arrayOf(
                                Channel(
                                        "Channel 1",
                                        "http://channel1.com",
                                        "First channel",
                                        "",
                                        listOf(
                                                Item("Ep01", "", "", Enclosure("", 0, ""), 0)
                                        )),
                                Channel(
                                        "Channel 2",
                                        "http://channel2.com",
                                        "Second channel",
                                        "",
                                        listOf(
                                                Item("Ep201", "", "", Enclosure("", 0, ""), 0)
                                        )),
                                Channel(
                                        "Channel 3",
                                        "http://channel3.com",
                                        "Third channel",
                                        "",
                                        listOf(
                                                Item("Ep301", "", "", Enclosure("", 0, ""), 0)
                                        ))
                        )
                ),
                row(
                        "a feed with itunes:image and a different way of formatting the date",
                        """
                            <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
                                <channel>
<atom:link rel="self" type="application/atom+xml" href="https://rss.simplecast.com/podcasts/1684/rss" title="MP3 Audio"/>
        <title>Fragmented - Android Developer Podcast</title>
        <generator>https://simplecast.com</generator>
        <description>The Fragmented Podcast is a podcast for Android Developers hosted by Donn Felker and Kaushik Gopal. Our goal is to help you become a better Android Developer. We chat about topics such as Testing, Dependency Injection, Android Patterns and Practices, useful libraries and much more. We will also be interviewing some of the top Android Developers out there. Subscribe now and join us on the journey of being an Android Developer.</description>
        <copyright>© 2016 Spec Network, Inc.</copyright>
        <language>en-us</language>
        <pubDate>Mon, 22 May 2017 05:00:00 +0000</pubDate>
        <lastBuildDate>Mon, 22 May 2017 19:01:57 +0000</lastBuildDate>
        <link>http://www.fragmentedpodcast.com</link>
        <image>
            <url>http://media.simplecast.com/podcast/image/1684/1474255312-artwork.jpg</url>
            <title>Fragmented - Android Developer Podcast</title>
            <link>http://www.fragmentedpodcast.com</link>
        </image>
        <itunes:author>Spec</itunes:author>
        <itunes:image href="http://media.simplecast.com/podcast/image/1684/1474255312-artwork.jpg"/>
        <itunes:summary>The Fragmented Podcast is a podcast for Android Developers hosted by Donn Felker and Kaushik Gopal. Our goal is to help you become a better Android Developer. We chat about topics such as Testing, Dependency Injection, Android Patterns and Practices, useful libraries and much more. We will also be interviewing some of the top Android Developers out there. Subscribe now and join us on the journey of being an Android Developer.</itunes:summary>
        <itunes:subtitle>The Fragmented Podcast is a podcast for Android Developers hosted by Donn Felker and Kaushik Gopal.</itunes:subtitle>
        <itunes:explicit>no</itunes:explicit>
        <itunes:keywords>android, developer, podcast, java, AndroidDev</itunes:keywords>
        <itunes:owner>
            <itunes:name>Spec Network, Inc.</itunes:name>
            <itunes:email>shows@spec.fm</itunes:email>
        </itunes:owner>
        <itunes:category text="Technology"/>
        <itunes:category text="Technology">
            <itunes:category text="Podcasting"/></itunes:category>
        <itunes:category text="Technology">
            <itunes:category text="Software How-To"/></itunes:category>
<item>
            <title>084: Kaush and Donn go to Google IO 2017</title>
            <guid isPermaLink="false">4bc01e71-dcc2-4793-967c-7776babb0ed5</guid>
            <description>This was truly one of the most memorable IOs Google has ever conducted. In keeping with tradition, Donn and Kaushik talk with a bunch of awesome #AndroidDev and get their opinions/thoughts on IO and Android in general. As always, these are super fun episodes.

Shownotes:  http://fragmentedpodcast.com/episodes/84/
</description>
            <content:encoded>
                <![CDATA[<p>This was truly one of the most memorable IOs Google has ever conducted. In keeping with tradition, Donn and Kaushik talk with a bunch of awesome #AndroidDev and get their opinions/thoughts on IO and Android in general. As always, these are super fun episodes.</p><a name="Show.Notes"></a><h2>Show Notes</h2><ul><li><strong>Dan Kim</strong> (<a href="https://basecamp.com/">Basecamp</a>)<ul><li><a href="https://twitter.com/dankim">@dankim</a> or<a href="mailto:dan@basecamp.com">dan@basecamp.com</a></li><li><a href="http://www.vysor.io/">Vysor app</a></li><li><a href="https://twitter.com/kaushikgopal/status/864925854219984896">Mark Allison - being brave with the O preview</a></li></ul></li><li><strong>Patryk Poborca</strong> (<a href="http://www.koziodigital.com/">Kozio Digital</a>)<ul><li><a href="https://twitter.com/patrykpoborca">@patrykpoborca</a></li><li><a href="https://play.google.com/store/apps/details?id=com.trello">Trello app</a></li></ul></li><li><strong>Jerrell Mardis</strong> (<a href="https://www.salesforce.com/">Salesforce</a>)<ul><li><a href="https://twitter.com/jerrellmardis">@jerrellmardis</a></li><li><a href="https://play.google.com/store/apps/details?id=com.robinhood.android&amp;hl=en">Robinhood app</a></li><li><a href="https://www.youtube.com/watch?v=FrteWKKVyzI">Android architecture components talk</a></li></ul></li><li><strong>Annyce Davis</strong> (<a href="http://offgrid-electric.com/#home">Offgrid Electric</a>)<ul><li><a href="https://twitter.com/brwngrldev">@brwngrldev</a></li><li><a href="https://play.google.com/store/apps/details?id=cc.forestapp">Forest app</a></li><li><a href="https://www.linkedin.com/learning/react-native-building-mobile-apps">React Native: Building Mobile apps</a></li></ul></li><li><strong>Zac Sweers</strong> (Uber)<ul><li><a href="https://twitter.com/pandanomic">@pandanomic</a></li><li><a href="https://play.google.com/store/apps/details?id=com.google.android.apps.youtube.unplugged">YouTube TV app</a></li><li><a href="https://play.google.com/store/apps/details?id=com.Slack">Slack app</a></li></ul></li><li><strong>Matt Kranzler</strong> (<a href="https://www.salesforce.com/">Salesforce</a>)<ul><li><a href="https://twitter.com/mattkranzler">@mattkranzler</a></li><li><a href="https://play.google.com/store/apps/details?id=com.samruston.twitter">Flamingo app</a></li></ul></li><li><strong>Roberto Orgiu</strong> (<a href="https://caster.io/">Caster</a>)<ul><li><a href="https://twitter.com/_tiwiz">@_tiwiz</a></li></ul></li><li><strong>Christina Lee</strong> (<a href="https://www.pinterest.com/">Pinterest</a>)<ul><li><a href="https://twitter.com/RunChristinaRun">@RunChristinaRun</a></li><li><a href="https://www.crowdrise.com/runningacrossamerica">Christina's Fundraiser - Running across America</a></li><li><a href="https://play.google.com/store/apps/details?id=com.robinhood.android&amp;hl=en">Robinhood app</a></li><li><a href="https://www.youtube.com/watch?v=fPzxfeDJDzY&amp;list=WL&amp;index=7">Talk at IO - Life is Great and Everything Will Be Ok, Kotlin is Here</a></li></ul></li><li><strong>Hugo Visser</strong> (<a href="http://littlerobots.nl/">Little Robots</a>)<ul><li><a href="https://twitter.com/botteaap">@botteaap</a> or<a href="https://plus.google.com/+HugoVisser">+HugoVisser</a></li><li><a href="https://bitbucket.org/hvisser/android-apt">Android APT</a></li></ul></li><li><strong>Raveesh Bhalla</strong><ul><li><a href="https://twitter.com/raveeshbhalla">@raveeshbhalla</a></li><li><a href="https://www.enki.com/">Enki app</a></li></ul></li><li><strong>Etienne Caron</strong> (<a href="https://www.shopify.com/">Shopify</a>)<ul><li><a href="https://twitter.com/kanawish">@kanawish</a></li><li><a href="https://play.google.com/store/apps/details?id=com.safariflow.queue">Safari Queue app</a></li></ul></li><li><strong>Chris Jenkins</strong> (<a href="http://owlr.com/">Owlr</a>)<ul><li><a href="https://twitter.com/chrisjenx">@chrisjenx</a></li><li><a href="https://github.com/chrisjenx/Calligraphy">Calligraphy font lib by Chris</a></li><li><a href="https://play.google.com/store/apps/details?id=com.waze">Waze app</a></li></ul></li><li><strong>Brenda Cook</strong> (<a href="https://web.seesaw.me/">Seesaw</a>)<ul><li><a href="https://twitter.com/kenodoggy">@kenodoggy</a></li><li><a href="https://play.google.com/store/apps/details?id=com.mint">Mint app</a></li><li><a href="https://play.google.com/store/apps/details?id=com.memrise.android.memrisecompanion">Memrise app</a></li></ul></li><li><strong>Joaquim</strong> (<a href="https://www.twitch.tv/">Twitch</a>)<ul><li><a href="https://twitter.com/joenrv">@joenrv</a></li><li><a href="https://news.realm.io/news/joaquim-verges-making-falcon-pro-3/">The Making of Falcon Pro 3</a></li><li><a href="http://getfalcon.pro/">Falcon Pro (Joaquim's app)</a></li><li><a href="https://play.google.com/store/apps/details?id=com.strafe.android">Strafe Esports app</a></li></ul></li></ul><a name="Previous.Google.IO.Episodes"></a><h2>Previous Google IO Episodes</h2><ul><li><a href="http://fragmentedpodcast.com/episodes/43">Ep 43 - Google IO 2016 (part 2)</a></li><li><a href="http://fragmentedpodcast.com/episodes/42/">Ep 42 - Google IO 2016 (part 1)</a></li><li><a href="http://fragmentedpodcast.com/episodes/9/">Ep 9 - Google IO 2015</a></li></ul><a name="Sponsors"></a><h2>Sponsors</h2><ul><li><a href="http://rollbar.com/fragmented">Rollbar - special offer: Bootstrap plan free for 90 days</a></li></ul><a name="Contact"></a><h2>Contact</h2><ul><li><a href="https://twitter.com/fragmentedcast">@fragmentedcast</a> [twitter.com]</li><li><a href="https://twitter.com/donnfelker">@donnfelker</a> and<a href="https://plus.google.com/+DonnFelker">+DonnFelker</a></li><li><a href="https://twitter.com/kaushikgopal/">@kaushikgopal</a> and<a href="https://plus.google.com/+KaushikGopalIsMe">+KaushikGopalIsMe</a></li></ul>

]]>

</content:encoded>
<pubDate>Mon, 22 May 2017 05:00:00 +0000</pubDate>
<author>shows@spec.fm (Spec)</author>
<enclosure url="http://audio.simplecast.com/70221.mp3" length="88341718" type="audio/mpeg"/>
<itunes:author>Spec</itunes:author>
<itunes:image href="http://media.simplecast.com/episode/image/70221/1495403043-artwork.jpg"/>
<itunes:duration>01:31:58</itunes:duration>
<itunes:summary>This was truly one of the most memorable IOs Google has ever conducted. In keeping with tradition, Donn and Kaushik talk with a bunch of awesome #AndroidDev and get their opinions/thoughts on IO and Android in general. As always, these are super fun episodes.

Shownotes:  http://fragmentedpodcast.com/episodes/84/
</itunes:summary>
<itunes:subtitle>This was truly one of the most memorable IOs Google has ever conducted. In keeping with tradition, Donn and Kaushik talk with a bunch of awesome #AndroidDev and get their opinions/thoughts on IO and Android in general. As always, these are super fun episo</itunes:subtitle>
<itunes:keywords>google io, IO, Android, Kotlin, announcement, conference</itunes:keywords>
<itunes:explicit>no</itunes:explicit>
</item>
</channel>
                            </rss>
                        """,
                        arrayOf(
                                Channel(
                                        "Fragmented - Android Developer Podcast",
                                        "http://www.fragmentedpodcast.com",
                                        "The Fragmented Podcast is a podcast for Android Developers hosted by Donn Felker and Kaushik Gopal. Our goal is to help you become a better Android Developer. We chat about topics such as Testing, Dependency Injection, Android Patterns and Practices, useful libraries and much more. We will also be interviewing some of the top Android Developers out there. Subscribe now and join us on the journey of being an Android Developer.",
                                        "http://media.simplecast.com/podcast/image/1684/1474255312-artwork.jpg",
                                        listOf(
                                                Item(
                                                        "084: Kaush and Donn go to Google IO 2017",
                                                        "",
                                                        """This was truly one of the most memorable IOs Google has ever conducted. In keeping with tradition, Donn and Kaushik talk with a bunch of awesome #AndroidDev and get their opinions/thoughts on IO and Android in general. As always, these are super fun episodes.

Shownotes:  http://fragmentedpodcast.com/episodes/84/
""",
                                                        Enclosure("http://audio.simplecast.com/70221.mp3", 88341718, "audio/mpeg"),
                                                        1495429200000
                                                )
                                        )
                                )
                        )
                )
        )

        forAll(errorResponsesTable) {
            Given("I will get an error $it") {
                val server = autoClose(MockWebServer())
                server.enqueue(MockResponse().setResponseCode(it))

                When("I request the channels of a feed") {
                    val subscriber = gateway.loadChannel(server.url("/").toString())
                            .test()

                    Then("I get an exception") {
                        subscriber.awaitTerminalEvent()
                        subscriber.assertNoValues()
                                .assertError(Exception::class.java)
                        val request = server.takeRequest()
                        request shouldNotBe null
                    }
                }
            }
        }

        forAll(successfulResponsesTable) { description, response, channels ->
            Given("I will get $description") {
                val server = autoClose(MockWebServer())
                server.enqueue(MockResponse().setBody(response))

                When("I request a feed") {
                    val subscriber = gateway.loadChannel(server.url("/").toString())
                            .test()

                    Then("I get the expected result") {
                        subscriber.awaitTerminalEvent()
                        subscriber.assertComplete()
                                .assertValues(*channels)

                        val request = server.takeRequest()
                        request shouldNotBe null
                    }
                }
            }
        }
    }

    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        test()
        RxJavaPlugins.reset()
    }
}