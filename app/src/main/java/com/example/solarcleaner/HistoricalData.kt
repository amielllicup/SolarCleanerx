package com.example.solarcleaner.data

data class HistoricalData(
    val timestamp: Long = System.currentTimeMillis(),
    val panel1Consumption: Int = 0,
    val panel1Harvest: Int = 0,
    val panel2Consumption: Int = 0,
    val panel2Harvest: Int = 0
)