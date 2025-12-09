package com.tugbaolcer.weatherapp.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tugbaolcer.weatherapp.data.remote.api.WeatherApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: WeatherApi


    private val fakeLat = 52.52
    private val fakeLon = 13.41

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `getCurrentWeather_successResponse_parsesCorrectly`() = runTest {
        val jsonResponse = """
            {
              "latitude": 52.52,
              "longitude": 13.41,
              "generationtime_ms": 0.518,
              "utc_offset_seconds": 0,
              "timezone": "Europe/Berlin",
              "timezone_abbreviation": "CET",
              "elevation": 37.0,
              "current_weather": {
                "temperature": 15.6,
                "windspeed": 10.5,
                "winddirection": 250,
                "weathercode": 3,
                "time": "2023-10-27T10:00",
                "is_day": 1
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // Çalıştırma
        val response = api.getCurrentWeather(fakeLat, fakeLon)

        /**
         * Kontrol: İstek yolu (path) doğru mu?
         */
        val recordedRequest = mockWebServer.takeRequest(5, TimeUnit.SECONDS)
        assertEquals("/v1/forecast?latitude=$fakeLat&longitude=$fakeLon&current_weather=true", recordedRequest?.path)

        /**
         * Kontrol: Gelen DTO doğru parse edildi mi?
         */

        assertEquals(fakeLat, response.latitude)
        assertEquals(15.6, response.currentWeather.temperature)
        assertEquals(250, response.currentWeather.windDirection)
    }

    @Test(expected = retrofit2.HttpException::class)
    fun `getCurrentWeather_serverError_throwsException`() = runTest {
        /**
         * 500 hatası döndüren mock yanıtı
          */

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Server Error")
        )

        /**
         * İstisnai durum bekleniyor
         */
        api.getCurrentWeather(fakeLat, fakeLon)
    }
}
