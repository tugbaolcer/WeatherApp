package com.tugbaolcer.weatherapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tugbaolcer.weatherapp.data.local.dao.WeatherDao
import com.tugbaolcer.weatherapp.data.local.database.WeatherDatabase
import com.tugbaolcer.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var db: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.weatherDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    private val testEntity = WeatherEntity(
        id = 0,
        latitude = 40.0,
        longitude = 30.0,
        temperature = 18.5,
        windSpeed = 5.0,
        windDirection = 90,
        generatedAtMs = System.currentTimeMillis()
    )

    /**
     * Kaydetme, ardından doğru okuma kontrolü
     */
    @Test
    fun `insertAndGetWeather_savesAndRetrievesCorrectly`() = runBlocking {
        dao.insertWeather(testEntity)
        val result = dao.getWeather()

        assertEquals(testEntity.temperature, result?.temperature)
        assertEquals(testEntity.windDirection, result?.windDirection)
    }

    /**
     * OnConflictStrategy.REPLACE'in doğru çalıştığı kontrolü
     */
    @Test
    fun `insertWeather_onConflictReplace_replacesOldData`() = runBlocking {
        val oldEntity = testEntity.copy(temperature = 5.0)
        dao.insertWeather(oldEntity)
        assertEquals(5.0, dao.getWeather()?.temperature)

        val newEntity = testEntity.copy(temperature = 25.0)
        dao.insertWeather(newEntity)
        val result = dao.getWeather()

        assertEquals(25.0, result?.temperature)
        assertEquals(1, db.query("SELECT COUNT(id) FROM weather", null).use { it.count })
    }

    /**
     * Veritabanı boşken null döndürme kontrolü
     */
    @Test
    fun `getWeather_emptyDatabase_returnsNull`() = runBlocking {
        val result = dao.getWeather()
        assertTrue(result == null)
    }
}