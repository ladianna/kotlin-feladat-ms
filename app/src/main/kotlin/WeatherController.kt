package hu.vanio.kotlin.feladat.ms

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
@RestController
class WeatherController {
    @GetMapping("/weather")
    fun getWeatherForecast(): ResponseEntity<Any> {
        val restTemplate = RestTemplate()
        val apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=47.4984&longitude=19.0404&hourly=temperature_2m&timezone=auto"

        return try {
            val response = restTemplate.getForEntity(apiUrl, WeatherForecast::class.java)
            if (response.statusCode == HttpStatus.OK) {
                val weatherForecast = response.body
                val dailyAverages = calculateDailyAverages(weatherForecast)
                printDailyAveragesToConsole(dailyAverages)
                ResponseEntity.ok(dailyAverages)
            } else {
                ResponseEntity.status(response.statusCode).body(null)
            }
        } catch (ex: ResourceAccessException) {
            println("Error accessing weather service: ${ex.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error accessing weather service: ${ex.message}")
        } catch (ex: Exception) {
            println("An unexpected error occurred: ${ex.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: ${ex.message}")
        }
    }

    private fun printDailyAveragesToConsole(dailyAverages: Map<LocalDate, Double>) {
        println("Average temperatures:")
        for ((date, averageTemperature) in dailyAverages) {
            println("$date: $averageTemperature")
        }
    }
}

fun calculateDailyAverages(weatherForecast: WeatherForecast?): Map<LocalDate, Double> {
    val hourlyData = weatherForecast?.hourly ?: return emptyMap()

    val dailyTemperatures = mutableMapOf<LocalDate, MutableList<Double>>()

    for (i in hourlyData.time.indices) {
        val localDateTime = LocalDateTime.parse(hourlyData.time[i])
        val date = localDateTime.toLocalDate()
        val temperature = hourlyData.temperature_2m[i]

        if (dailyTemperatures.containsKey(date)) {
            dailyTemperatures[date]!!.add(temperature)
        } else {
            dailyTemperatures[date] = mutableListOf(temperature)
        }
    }

    return dailyTemperatures.mapValues { entry ->
        entry.value.average()
    }
}
