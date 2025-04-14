package dev.zt64.budgeteer.domain.converter

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class InstantConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let {
            Instant.fromEpochMilliseconds(it)
        }
    }

    @TypeConverter
    fun toTimestamp(dateTime: Instant?): Long? {
        return dateTime?.toEpochMilliseconds()
    }
}