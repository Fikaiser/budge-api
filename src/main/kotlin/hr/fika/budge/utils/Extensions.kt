package hr.fika.budge.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Int.epochToDateTime() = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.toLong()), ZoneId.systemDefault())