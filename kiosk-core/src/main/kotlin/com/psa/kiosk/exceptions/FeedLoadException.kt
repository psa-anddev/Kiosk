package com.psa.kiosk.exceptions

/**
 * This exception notifies the application that there was a
 * problem loading a feed.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class FeedLoadException(val url : String, cause : Throwable) :
        Exception("Loading of $url failed", cause)