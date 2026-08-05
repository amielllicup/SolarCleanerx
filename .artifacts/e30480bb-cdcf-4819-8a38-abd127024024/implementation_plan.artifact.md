# Implementation Plan - Automated History Logging

This plan implements automatic background logging of solar data to the Firebase Realtime Database history table.

## User Review Required

> [!CAUTION]
> This logging is handled by the app. If multiple devices are running the app simultaneously, the logging logic will include a "cooldown" check to prevent duplicate entries for the same time period.

## Proposed Changes

### Data Layer

#### [MODIFY] [FirebaseRepository.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/data/FirebaseRepository.kt)
- **New Logging Logic**: Add `autoLogHistory(data: SolarLiveData)` function.
- **Duplicate Prevention**:
    - This function will fetch the last entry from the `history` node.
    - It will only write a new record if the current time is at least **5 minutes** later than the last recorded entry.
    - This ensures a clean, consistent history trail (12 points per hour) without overwhelming the database.

### UI Integration

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Background Logger**: In `MainApp`, add a `LaunchedEffect` that monitors `solarLiveData`.
- **Trigger**: Every time the live data updates from the hardware, the app will attempt to log it via `repository.autoLogHistory`.
- **Visual Feedback**: Ensure the **Live Chart** (which filters for the current day) and the **Records** tab instantly reflect these new automated entries.

## Verification Plan

### Manual Verification
- Deploy the app and keep it open.
- Check the Firebase Console to see if a new entry is created in the `history` node after 5 minutes.
- Verify that closing and reopening the app doesn't create immediate duplicate entries (due to the 5-minute cooldown).
- Confirm that the **Live Power Monitoring** chart grows automatically as new points are added to the history.
