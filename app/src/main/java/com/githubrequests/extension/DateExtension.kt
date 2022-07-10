package com.githubrequests.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.getTimeInUtcFormat(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val convertDate = simpleDateFormat.parse(this)
    return SimpleDateFormat("MM-dd-yyyy").format(convertDate)
}