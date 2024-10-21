package com.example.reselover.note.presentance.utils

import android.net.Uri

object Utils {
    const val TABLE_NAME = "Notes"
    const val AUTHORITY = "com.example.provider.notes.data.provider"
    val CONTENT_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
}