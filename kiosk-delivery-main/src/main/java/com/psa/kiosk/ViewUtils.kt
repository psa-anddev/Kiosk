package com.psa.kiosk

import android.support.design.widget.Snackbar
import android.view.View

/**
 * A collection of extension functions to make view usual tasks easier.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */

/**
 * Shows a snackbar with the given message and duration.
 *
 * @param message is the message to display.
 * @param duration is the duration as specified in the Snackbar API.
 */
fun View.snack(message : CharSequence, duration : Int) {
    Snackbar.make(this, message, duration).show()
}
