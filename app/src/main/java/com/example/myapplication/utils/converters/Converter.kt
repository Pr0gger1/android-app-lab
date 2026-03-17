package com.example.myapplication.utils.converters

interface Converter<T> {
    fun toString(data: T): String

    fun fromString(data: String): T
}