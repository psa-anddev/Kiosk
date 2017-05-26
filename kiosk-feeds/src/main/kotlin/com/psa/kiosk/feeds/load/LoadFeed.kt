package com.psa.kiosk.feeds.load

import com.psa.kiosk.KioskContext.channelsGateway
import com.psa.kiosk.exceptions.FeedLoadException
import io.reactivex.Single

/**
 * Implementation of the interactor that loads a feed.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class LoadFeed : LoadFeedInput {
    override fun execute(request: LoadFeedRequest, output: LoadFeedOutput) {
        channelsGateway.loadChannel(request.url)
                .toList()
                .map { LoadFeedResponse(it) }
                .onErrorResumeNext { t: Throwable -> Single.error(FeedLoadException(request.url, t)) }
                .subscribe({ output.generateViewModel(it)},
                        { output.generateViewModel(it as FeedLoadException) })
    }
}