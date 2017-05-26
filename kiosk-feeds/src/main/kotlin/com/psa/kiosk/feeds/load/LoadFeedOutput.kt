package com.psa.kiosk.feeds.load

import com.psa.kiosk.exceptions.FeedLoadException

/**
 * This is the output boundary for the load feed use case.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
interface LoadFeedOutput {
    fun generateViewModel(response: LoadFeedResponse)
    fun generateViewModel(exception : FeedLoadException)
}