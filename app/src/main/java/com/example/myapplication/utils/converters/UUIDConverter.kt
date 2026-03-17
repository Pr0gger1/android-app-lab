package com.example.myapplication.utils.converters

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter : Converter<UUID> {
    @TypeConverter
    override fun toString(data: UUID): String {
        return data.toString()
    }

    @TypeConverter
    override fun fromString(data: String): UUID {
        return UUID.fromString(data)
    }
}
