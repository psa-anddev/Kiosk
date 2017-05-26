package com.psa.kiosk.network.services

import com.psa.kiosk.network.responses.FeedResponse
import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * This service provide RSS data from a given URL using
 * Retrofit.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
interface RSSService {
    @GET
    fun getFeed(@Url feedUrl: HttpUrl?) : Single<FeedResponse>
}