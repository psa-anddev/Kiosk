package com.psa.kiosk.feeds

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.psa.kiosk.R
import com.psa.kiosk.channel.ChannelsViewModel
import kotlinx.android.synthetic.main.dialog_load_feed.*

/**
 * This dialog allows the user to update the feed that is currently visible.
 *
 * @author Pablo Sánchez Alonso
 * @since 1.0
 */
class UpdateFeedDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(activity)
                .setView(R.layout.dialog_load_feed)
                .setPositiveButton(R.string.load_feed) {dialogInterface, i ->
                    ViewModelProviders.of(activity)[ChannelsViewModel::class.java]
                            .loadFeed((dialogInterface as AlertDialog).feedUrl?.editText?.text?.toString()?:"")
                }
                .create()
}