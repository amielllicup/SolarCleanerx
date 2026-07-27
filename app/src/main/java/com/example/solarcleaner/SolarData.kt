package com.example.solarcleaner.data

import com.google.firebase.database.Exclude

data class SolarData(
    var cleanerStatus: Boolean = false,
    var panel1Consumption: Int = 65,
    var panel1Harvest: Int = 500,
    var panel2Consumption: Int = 55,
    var panel2Harvest: Int = 220,
    var timestamp: Long = System.currentTimeMillis()
) {
    @Exclude
    fun getTotalConsumption(): Int = panel1Consumption + panel2Consumption

    @Exclude
    fun getTotalHarvest(): Int = panel1Harvest + panel2Harvest

    @Exclude
    fun getEfficiency(): Float = if (getTotalHarvest() > 0) {
        (getTotalConsumption().toFloat() / getTotalHarvest().toFloat()) * 100
    } else 0f
}