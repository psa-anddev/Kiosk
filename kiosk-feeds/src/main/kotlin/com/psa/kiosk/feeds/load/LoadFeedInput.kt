package com.psa.kiosk.feeds.load

/**
 * Input boundary for the load feed use case.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
interface LoadFeedInput {
    fun execute(request: LoadFeedRequest, output: LoadFeedOutput)
}