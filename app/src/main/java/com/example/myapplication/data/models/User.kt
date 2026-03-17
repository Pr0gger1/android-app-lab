package com.example.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.utils.converters.UUIDConverter
import java.util.UUID

@Entity(tableName = "users")
@TypeConverters(
    UUIDConverter::class
)
data class User(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String,
    var email: String,
    var password: String
)
