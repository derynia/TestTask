package com.android.testtask.utils

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity

const val STATION_SAVE_ERROR = "Station save error"

fun showError(activity: FragmentActivity?, title: String, text: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
    builder.setTitle(title)
        .setMessage(text)
        .setPositiveButton(
            "OK"
        ) { dialog, _ ->
            dialog.cancel()
        }

    builder.create()
    builder.show()
}