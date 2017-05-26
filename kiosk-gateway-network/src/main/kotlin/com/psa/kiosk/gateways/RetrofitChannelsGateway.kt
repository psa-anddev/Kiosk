package com.psa.kiosk.gateways

import com.psa.kiosk.entities.Channel
import com.psa.kiosk.entities.Enclosure
import com.psa.kiosk.entities.Item
import com.psa.kiosk.network.models.ChannelModel
import com.psa.kiosk.network.models.EnclosureModel
import com.psa.kiosk.network.models.ItemModel
import com.psa.kiosk.network.services.RSSService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import org.joda.time.format.DateTimeFormat
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * A channels gateway that uses Retrofit to gather the channels data.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class RetrofitChannelsGateway : ChannelsGateway {
    val rssService: RSSService =
            Retrofit.Builder()
                    .baseUrl("http://server.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
                    .build()
                    .create(RSSService::class.java)


    override fun loadChannel(feedUrl: String): Observable<Channel> =
            rssService.getFeed(HttpUrl.parse(feedUrl))
                    .subscribeOn(Schedulers.io())
                    .flatMapObservable { Observable.fromIterable(it.channels) }
                    .map { mapModelToChannel(it) }

    private fun mapModelToChannel(model: ChannelModel) =
            Channel(
                    model.title,
                    model.link,
                    model.description,
                    model.image.url,
                    model.items.map { mapModelToItem(it) }
            )

    private fun mapModelToItem(model: ItemModel) =
            Item(
                    model.title,
                    model.link,
                    model.description,
                    mapModelToEnclosure(model.enclosure),
                    mapModelToPublicationDate(model.pubDate)
            )

    private fun mapModelToEnclosure(model: EnclosureModel) =
            Enclosure(
                    model.url,
                    model.length.toLong(),
                    model.type)

    private fun mapModelToPublicationDate(model: String) =
            if (model == "")
                0
            else
                try {
                    getRFC822DateFormatter().parseMillis(model)
                }
                catch (ex : Throwable) {
                    getTwitterDateFormatter().parseMillis(model)
                }

    private fun getRFC822DateFormatter() =
            DateTimeFormat.forPattern("E, d MMM y HH:mm:ss z")
    private fun getTwitterDateFormatter() =
            DateTimeFormat.forPattern("E, d MMM y HH:mm:ss Z")

}