package com.example.solarcleaner.repository

import android.util.Log
import com.example.solarcleaner.data.HistoricalData
import com.example.solarcleaner.data.SolarData
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val solarDataRef = database.getReference("solarData")
    private val historyRef = database.getReference("history")
    private val cleanerStatusRef = database.getReference("cleanerStatus")

    // Real-time listener for solar data
    fun observeSolarData(): Flow<SolarData> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(SolarData::class.java) ?: SolarData()
                trySend(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseRepo", "Error observing solar data: ${error.message}")
                close(error.toException())
            }
        }

        solarDataRef.addValueEventListener(listener)

        awaitClose {
            solarDataRef.removeEventListener(listener)
        }
    }

    // Read historical data
    suspend fun getHistoricalData(limit: Int = 20): List<HistoricalData> {
        return try {
            val snapshot = historyRef
                .orderByChild("timestamp")
                .limitToLast(limit)
                .get()
                .await()

            snapshot.children.mapNotNull {
                it.getValue(HistoricalData::class.java)
            }.reversed()
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error getting historical data: ${e.message}")
            emptyList()
        }
    }

    // Update cleaner status
    suspend fun updateCleanerStatus(status: Boolean): Boolean {
        return try {
            cleanerStatusRef.setValue(status).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating cleaner status: ${e.message}")
            false
        }
    }

    // Write new solar data (for testing/ESP32)
    suspend fun writeSolarData(data: SolarData): Boolean {
        return try {
            solarDataRef.setValue(data).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error writing solar data: ${e.message}")
            false
        }
    }

    // Add historical data point (for testing)
    suspend fun addHistoricalData(data: HistoricalData): Boolean {
        return try {
            val key = historyRef.push().key
            if (key != null) {
                historyRef.child(key).setValue(data).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error adding historical data: ${e.message}")
            false
        }
    }
}