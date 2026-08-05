# Implementation Plan - Categorical Bar Chart UI

This plan replaces the time-series line graph with a categorical bar chart (column chart) showing the current system metrics: Consumption, S1 Harvest, and S2 Harvest.

## Proposed Changes

### [Component] Live Power Chart

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- **Layer Transition**:
    - Replace `rememberLineCartesianLayer` with `rememberColumnCartesianLayer`.
    - Configure the `ColumnProvider` with 3 distinct components:
        - **Consumption**: Orange, rounded top corners.
        - **S1 Harvest**: Blue, rounded top corners.
        - **S2 Harvest**: Blue (or Green), rounded top corners.
- **Categorical Data**:
    - Update `modelProducer` to use `columnSeries`.
    - Provide 3 separate series, each with a single data point at a unique index (0, 1, 2).
- **X-Axis Labels**:
    - Implement a `CartesianValueFormatter` for the horizontal axis that returns:
        - Index 0 -> "Consumption"
        - Index 1 -> "S1 Harvest"
        - Index 2 -> "S2 Harvest"
- **Scaling & Scroll**:
    - Ensure `scrollEnabled = false` and use `zoomEnabled = false` to keep the 3 bars fixed and centered.
    - Set a fixed `columnWidth` and `columnCollectionSpacing` to match the spacing in the reference image.

## Verification Plan

### Manual Verification
- Deploy the app and verify the chart shows 3 distinct bars.
- Confirm the bars have rounded tops and colors match the request.
- Verify the X-axis labels correctly identify each bar.
- Ensure the bars update in real-time as the Firebase data changes.
