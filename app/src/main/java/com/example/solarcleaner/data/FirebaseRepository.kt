package com.example.solarcleaner.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

data class SolarLiveData(
    val batteryPercent: Double = 0.0,
    val batteryVoltage: Double = 0.0,
    val consumptionPercent: Double = 0.0,
    val harvestPercent: Double = 0.0,
    val harvestVoltage: Double = 0.0,
    val relayMode: Int = 0,
    val remainingEnergy: Double = 0.0,
    val solar1Harvest: Double = 0.0,
    val solar1Voltage: Double = 0.0,
    val solar2Harvest: Double = 0.0,
    val solar2Voltage: Double = 0.0
)

data class DustSensorData(
    val dustDensity: Double = 0.0,
    val raw: Double = 0.0,
    val status: String = "",
    val voltage: Double = 0.0
)

class FirebaseHistoryRecord() {
    var batteryPercent: Any? = 0.0
    var batteryVoltage: Any? = 0.0
    var consumptionPercent: Any? = 0.0
    var harvestPercent: Any? = 0.0
    var harvestVoltage: Any? = 0.0
    var remainingEnergy: Any? = 0.0
    var solar1Harvest: Any? = 0.0
    var solar1Voltage: Any? = 0.0
    var solar2Harvest: Any? = 0.0
    var solar2Voltage: Any? = 0.0
    var timestamp: Any? = 0L

    // Cache the parsed timestamp to prevent repeated expensive parsing
    val cachedTimestamp: Long by lazy { timestamp.toSafeLong() }

    fun getSafeTimestamp(): Long = cachedTimestamp
    fun getSafeCons(): Double = consumptionPercent.toSafeDouble()
    fun getSafeS1(): Double {
        val s1 = solar1Harvest.toSafeDouble()
        return if (s1 > 0) s1 else harvestPercent.toSafeDouble()
    }
    fun getSafeS2(): Double {
        val s2 = solar2Harvest.toSafeDouble()
        if (s2 > 0) return s2
        val s2V = solar2Voltage.toSafeDouble()
        return if (s2V > 0) s2V else harvestVoltage.toSafeDouble()
    }

    private fun Any?.toSafeLong(): Long {
        return when (this) {
            is Long -> this
            is Double -> this.toLong()
            is Int -> this.toLong()
            is String -> {
                this.toLongOrNull() ?: try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    sdf.parse(this)?.time ?: 0L
                } catch (e: Exception) {
                    0L
                }
            }
            else -> 0L
        }
    }

    private fun Any?.toSafeDouble(): Double {
        return when (this) {
            is Double -> this
            is Float -> this.toDouble()
            is Long -> this.toDouble()
            is Int -> this.toDouble()
            is String -> this.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
    }
}

data class FirebaseCleaningRecord(
    val action: String = "",
    val status: String = "",
    val timestamp: Any? = 0L
) {
    fun getSafeTimestamp(): Long {
        return when (val t = timestamp) {
            is Long -> t
            is Double -> t.toLong()
            is String -> t.toLongOrNull() ?: try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                sdf.parse(t)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
            is Int -> t.toLong()
            else -> 0L
        }
    }
}

class FirebaseRepository {
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val solarDataRef by lazy { database.getReference("SolarMonitor") }
    private val cleanerStatusRef by lazy { database.getReference("cleanerStatus") }
    private val historyRef by lazy { database.getReference("history") }
    private val cleaningHistoryRef by lazy { database.getReference("cleaningHistory") }
    private val dustSensorRef by lazy { database.getReference("dustSensor") }

    fun getSolarLiveData(): Flow<SolarLiveData?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(SolarLiveData::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = solarDataRef
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getCleanerStatus(): Flow<Boolean> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Boolean::class.java) ?: false)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = cleanerStatusRef
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getDustSensorData(): Flow<DustSensorData?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(DustSensorData::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dustSensorRef.addValueEventListener(listener)
        awaitClose { dustSensorRef.removeEventListener(listener) }
    }

    fun toggleCleaner(status: Boolean) {
        cleanerStatusRef.setValue(status)
        // Also update the status inside solarData to keep them in sync if needed
        solarDataRef.child("cleanerStatus").setValue(status)
        
        // Auto log to cleaningHistory
        val record = FirebaseCleaningRecord(
            action = if (status) "Start" else "Stop",
            status = if (status) "Active" else "Standby",
            timestamp = System.currentTimeMillis()
        )
        cleaningHistoryRef.push().setValue(record)
    }

    fun getHistory(): Flow<List<FirebaseHistoryRecord>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val records = snapshot.children.mapNotNull { 
                    it.getValue(FirebaseHistoryRecord::class.java) 
                }
                trySend(records)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = historyRef
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }.map { list ->
        list.sortedByDescending { it.cachedTimestamp }
    }.flowOn(Dispatchers.Default)

    fun getCleaningHistory(): Flow<List<FirebaseCleaningRecord>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val records = snapshot.children.mapNotNull {
                    it.getValue(FirebaseCleaningRecord::class.java)
                }.reversed()
                trySend(records)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = cleaningHistoryRef
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
    
    fun addHistoryRecord(record: FirebaseHistoryRecord) {
        historyRef.push().setValue(record)
    }
}
