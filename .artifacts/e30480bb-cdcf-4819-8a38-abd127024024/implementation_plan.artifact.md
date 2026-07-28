# Implementation Plan - Fixed Height Scrollable Tables

This plan modifies the **Daily Records** and **Cleaning History** screens to have a fixed height for the data tables, making them scrollable only within that restricted area.

## Proposed Changes

### [Component] Daily Records Screen

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- Convert the main container from a `LazyColumn` to a standard `Column`.
- Keep the Title, Subtitle, and Filter row at the top.
- Place the **Table Header** and **Data Rows** inside a dedicated container with a fixed height (e.g., 400.dp).
- Use a `LazyColumn` for the data rows within this fixed-height container to ensure only the table content scrolls.

### [Component] Cleaning History Screen

#### [MODIFY] [MainActivity.kt](file:///C:/Users/TESDA-IT/StudioProjects/SolarCleanerx/app/src/main/java/com/example/solarcleaner/MainActivity.kt)
- Similar to the Records screen, convert the main layout to a `Column`.
- Move the **Table Header** out of the scrollable area so it remains fixed at the top of the table.
- Wrap the cleaning log rows in a `LazyColumn` with a fixed height.

## Verification Plan

### Manual Verification
- Deploy to device.
- Navigate to "Records" and "Cleaning" tabs.
- Verify that the screen headers (Title/Filters) stay fixed.
- Verify that the table has a defined height and you can scroll through the records within that box.
- Confirm the layout looks consistent with the "Premium Modern" aesthetic.
