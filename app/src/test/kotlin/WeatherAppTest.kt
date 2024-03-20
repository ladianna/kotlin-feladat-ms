package hu.vanio.kotlin.feladat.ms

import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate
import org.junit.jupiter.api.Test

class WeatherAppTest {

    @Test fun `sikeres lekerdezes`() {
        // Given
        val hourlyData = HourlyData(
                listOf("2024-03-18T00:00:00", "2024-03-18T01:00:00", "2024-03-18T02:00:00"),
                listOf(10.0, 12.0, 14.0)
        )
        val weatherForecast = WeatherForecast(0.0, 0.0, "UTC", hourlyData)

        // When
        val dailyAverages = calculateDailyAverages(weatherForecast)

        // Then
        val expectedDailyAverages = mapOf(
                LocalDate.of(2024, 3, 18) to 12.0
        )
        assertEquals(expectedDailyAverages, dailyAverages)
    }
}