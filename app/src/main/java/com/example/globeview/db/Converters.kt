package com.example.globeview.db

import androidx.room.TypeConverter
import com.example.globeview.models.Href
import com.example.globeview.models.Link
import com.example.globeview.models.President
import com.example.globeview.models.PresidentHref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

//    @TypeConverter
//    fun fromSource(link: Link): String {
//        return link.href
//    }
//
//    @TypeConverter
//    fun toLink(link: String): Link {
//        return Link(link, link)
//    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromMyCustomObject(value: Href?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toMyCustomObject(value: String?): Href? {
        return value?.let {
            val type = object : TypeToken<Href>() {}.type
            Gson().fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromPresidentObject(value: President?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toPresidentObject(value: String?): President? {
        return value?.let {
            val type = object : TypeToken<President>() {}.type
            Gson().fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromPresidentHrefObject(value: PresidentHref?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toPresidentHrefObject(value: String?): PresidentHref? {
        return value?.let {
            val type = object : TypeToken<PresidentHref>() {}.type
            Gson().fromJson(it, type)
        }
    }



}