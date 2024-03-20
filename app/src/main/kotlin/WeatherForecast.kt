package hu.vanio.kotlin.feladat.ms
data class WeatherForecast(
        val latitude: Double,
        val longitude: Double,
        val timezone: String,
        val hourly: HourlyData
)
data class HourlyData(
        val time: List<String>,
        val temperature_2m: List<Double>
)

