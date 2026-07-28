package com.example.solarcleaner.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

data class SolarLiveData(
    val cleanerStatus: Boolean = false,
    val panel1Consumption: Int = 0,
    val panel1Harvest: Int = 0,
    val panel2Consumption: Int = 0,
    val panel2Harvest: Int = 0,
    val timestamp: Long = 0
)

data class FirebaseHistoryRecord(
    val panel1Consumption: Int = 0,
    val panel1Harvest: Int = 0,
    val panel2Consumption: Int = 0,
    val panel2Harvest: Int = 0,
    val timestamp: Long = 0
)

data class FirebaseCleaningRecord(
    val action: String = "",
    val status: String = "",
    val timestamp: Long = 0
)

class FirebaseRepository {
    private val database by lazy { FirebaseDatabase.getInstance() }
    private val solarDataRef by lazy { database.getReference("solarData") }
    private val cleanerStatusRef by lazy { database.getReference("cleanerStatus") }
    private val historyRef by lazy { database.getReference("history") }
    private val cleaningHistoryRef by lazy { database.getReference("cleaningHistory") }

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
                }.reversed()
                trySend(records)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = historyRef
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

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
