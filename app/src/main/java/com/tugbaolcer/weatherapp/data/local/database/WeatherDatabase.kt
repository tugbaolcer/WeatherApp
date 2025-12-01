package com.tugbaolcer.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tugbaolcer.weatherapp.data.local.dao.WeatherDao
import com.tugbaolcer.weatherapp.data.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
