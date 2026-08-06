# Implementation Plan - Fix Records History Discrepancy

This plan resolves the issue where the **Daily Records** table shows 0% values despite the **Live Dashboard** showing active data (100% consumption, etc.).

## Proposed Changes

### Data Layer

#### [MODIFY] [FirebaseRepository.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/data/FirebaseRepository.kt)
- **Improve `autoLogHistory`**:
    - **Validity Check**: Add a check to ensure we only log data if at least one primary metric (Consumption or Harvest) is non-zero. This prevents logging the initial "all-zero" state when the app first connects to Firebase.
    - **Reliability**: Use the current system time only for the local cooldown check, but ensure the data object being saved contains the correct live values.

### UI Integration

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Refine Logging Trigger**: In `MainApp`, add a small delay or validation to the `LaunchedEffect` that triggers `autoLogHistory`. This ensures that if `solarLiveData` is rapidly updating from 0 to 100, we don't accidentally lock the 1-minute cooldown on the 0 value.
- **Records Table Formatting**: Ensure the `s2Harvest` column in the records table uses the same formatting logic as the dashboard (displaying as "V" instead of "%" if necessary) to maintain consistency.

## Verification Plan

### Manual Verification
- Deploy to device and verify the **Live Dashboard** shows active data (e.g., 100% Cons).
- Navigate to the **Records** tab and confirm that the latest record reflects the same active data (100% Cons).
- Check the Firebase Console to ensure no "all-zero" records are being written to the `history` node during app startup.
- Verify that every point on the **Line Graph** has a corresponding correct row in the **Daily Records** table.
