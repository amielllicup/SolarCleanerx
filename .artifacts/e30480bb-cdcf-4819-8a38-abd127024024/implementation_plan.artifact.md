# Implementation Plan - Premium Modern Dashboard UI

This plan aims to refine the Dashboard and Live Power Chart UI to achieve a "premium modern look" based on the provided reference image.

## Proposed Changes

### [Component] Theming & Layout

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Card Background**: Update `CardContainer` to use a darker, more defined background color (matching the "Premium" aesthetic in the image).
- **Typography**: Use larger, bolder fonts for primary metrics like "Total Power".

### [Component] Live Power Chart (Vico Enhancement)

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Grid Lines**: Configure `VerticalAxis` and `HorizontalAxis` to use **dashed guidelines** (`dashedShape()`) for a clean, technical look.
- **Line Styling**:
    - Ensure lines are smooth (though standard lines are also fine if styled correctly).
    - Refine **Area Fills**: Use subtle gradients that fade into the background.
    - **Point Markers**: Size them to match the "Pill" shape in the image, ensuring they are prominent but not distracting.
- **X-Axis**: Format labels as hours/indices as seen in the reference image (4, 5, 6, ...).

### [Component] Live Stats Bar

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Stats Styling**:
    - Increase font size for the numeric values (e.g., "1100 W").
    - Match colors exactly: Orange for Total Power, Blue for Harvest, Green for Efficiency.
    - Ensure icons are perfectly aligned and sized.

## Verification Plan

### Manual Verification
- Deploy to emulator/device.
- Compare the live app side-by-side with the user's reference image.
- Verify the "Live" status dot is correctly positioned and pulsing if possible.
- Ensure the chart remains responsive with the new styling.
