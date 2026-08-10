# Implementation Plan - Automated Cleaning Trigger (High Dust)

Add logic to automatically start the cleaning cycle when the dust sensor detects a "HIGH DUST" condition.

## Proposed Changes

### Data Layer

#### [MODIFY] [FirebaseRepository.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/data/FirebaseRepository.kt)
- **New Data Model**: Add `DustSensorData` class to handle fields: `dustDensity`, `raw`, `status`, `voltage`.
- **New Listener**: Add `getDustSensorData()` to observe the `/dustSensor` node in Firebase.

### Application Logic

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Observe Dust Level**: In `MainApp`, collect the new `dustSensorData` stream from the repository.
- **Automated Trigger**: Implement a `LaunchedEffect` that monitors the dust sensor status.
    - **Logic**: If `status == "HIGH DUST"` AND `cleanerOn == false`, automatically call `repository.toggleCleaner(true)`.
    - **Safety**: Ensure this only triggers once when the threshold is crossed to prevent multiple duplicate "Start" commands in the log.

## Verification Plan

### Manual Verification
- **Simulated Test**: Use the Firebase Console to manually change `/dustSensor/status` to `"HIGH DUST"`.
- **Observation**: Verify that the app's "Cleaner Control" button automatically switches to "Stop Cleaning Cycle" and the hardware node `cleanerStatus` becomes `true`.
- **Log Check**: Verify that a "Start" action is recorded in the `cleaningHistory` node.
- **Reset Test**: Change status back to `"CLEAN"` and verify the cleaner doesn't automatically turn off (as per previous requirement: user must turn it off manually).
