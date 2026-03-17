package com.example.myapplication.utils.converters

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun toStringUUID(id: UUID): String {
        return id.toString()
    }

    @TypeConverter
    fun fromStringToUUID(stringId: String): UUID {
        return UUID.fromString(stringId)
    }
}
