# Implementation Plan - Force Low Resolution & Fix Camera Black Screen

This plan forces the ESP32-CAM into its lowest resolution and simplifies the streaming logic to resolve the persistent black screen issue.

## Proposed Changes

### [Component] Camera Screen Enhancements

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Simplify Connection Phase**:
    - Focus strictly on setting the resolution to **Low (320x240)** during the connection process.
    - Add a manual **"Set Low Resolution"** button to the UI for troubleshooting.
- **Direct Stream Loading**:
    - Switch the `WebView` from an HTML wrapper back to a direct URL load for the `/stream` endpoint. Some MJPEG streams are more stable when loaded directly.
- **Resolution Feedback**:
    - Add buttons for **"Low"**, **"Mid"**, and **"High"** resolutions under the camera card.
    - These buttons will send independent commands to the ESP32-CAM and then trigger a stream refresh.
- **Enhanced Settings**:
    - Configure the `WebView` to be more resilient (disable cache, enable zoom controls for inspection).

## Verification Plan

### Manual Verification
- **Low-Res Test**: Click "Connect" and confirm if the low-resolution stream appears.
- **Manual Toggle**: Click the "Low" resolution button below the camera and verify if the black screen resolves.
- **Direct Link Test**: Check the "Active Stream" URL displayed at the bottom to ensure it points correctly to your camera's port and path.
