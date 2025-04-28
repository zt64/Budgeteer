package dev.zt64.budgeteer.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Instant
import kotlin.time.toJavaInstant

internal object Formatter {
    fun formatNumber(amount: Double, places: Int = 2): String {
        val f = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = places
            maximumFractionDigits = places
        }
        return f.format(amount)
    }

    fun formatMoney(amount: Float): String {
        return formatMoney(amount.toDouble())
    }

    fun formatMoney(amount: Double): String {
        return "$${formatNumber(amount)}"
    }

    fun formatInstant(instant: java.time.Instant): String {
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return "${localDateTime.year}-${String.format("%02d", localDateTime.monthValue)}-${String.format("%02d", localDateTime.dayOfMonth)}"
    }

    fun formatDate(millisSinceEpoch: Long): String {
        val instant = java.time.Instant.ofEpochMilli(millisSinceEpoch)
        return formatInstant(instant)
    }

    fun formatDate(date: Instant): String {
        return DateTimeFormatter
            .ofPattern("MMMM dd, yyyy h:mm a")
            .withZone(ZoneId.systemDefault())
            .format(date.toJavaInstant())
    }

    fun formatTime(hour: Int, minute: Int): String {
        return DateTimeFormatter
            .ofPattern("h:mm a")
            .withZone(ZoneId.systemDefault())
            .format(LocalTime.of(hour, minute).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant())
    }
}