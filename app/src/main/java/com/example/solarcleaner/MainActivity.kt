package com.example.solarcleaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.solarcleaner.data.HistoricalData
import com.example.solarcleaner.data.SolarData
import com.example.solarcleaner.repository.FirebaseRepository
import com.example.solarcleaner.ui.theme.SolarCleanerTheme
import com.example.solarcleaner.ui.theme.SolarBlue
import com.example.solarcleaner.ui.theme.SolarGreen
import com.example.solarcleaner.ui.theme.SolarOrange
import com.example.solarcleaner.ui.theme.SolarSun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

private const val LoginEmail = "admin@solarglide.com"
private const val LoginPassword = "Admin@1234"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SolarCleanerTheme {
                SolarGlideApp()
            }
        }
    }
}

@Composable
private fun SolarGlideApp() {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Crossfade(targetState = isLoggedIn, animationSpec = tween(800)) { loggedIn ->
            if (loggedIn) {
                MainApp(onLogout = { isLoggedIn = false })
            } else {
                LoginScreen(onLoginSuccess = { isLoggedIn = true })
            }
        }
    }
}

@Composable
private fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SolarGlideLogo(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to SolarGlide",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Automatic Solar Panel Cleaning",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            CardContainer {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Hide" else "Show", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))
                Button(
                    onClick = {
                        if ((email.trim() == LoginEmail) && (password == LoginPassword)) {
                            errorMessage = null
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid email or password."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Login", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainApp(onLogout: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Dashboard) }
    var cleanerOn by rememberSaveable { mutableStateOf(false) }
    val cleaningHistory = remember { mutableStateListOf<String>() }

    // Initialize Firebase Repository
    val repository = remember { FirebaseRepository() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SolarGlideLogo(
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("SolarGlide", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 12.dp
            ) {
                AppTab.entries.forEach { tab ->
                    val selected = selectedTab == tab
                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.2f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "NavScale"
                    )

                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier
                                    .size(26.dp)
                                    .graphicsLayer(scaleX = scale, scaleY = scale)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            Crossfade(targetState = selectedTab, animationSpec = tween(500)) { tab ->
                when (tab) {
                    AppTab.Dashboard -> DashboardScreen(
                        cleanerOn = cleanerOn,
                        onToggleCleaner = {
                            cleanerOn = !cleanerOn
                            val dateFormat = SimpleDateFormat("MMMM d, yyyy - HH:mm", Locale.getDefault())
                            val timestamp = dateFormat.format(Date())
                            cleaningHistory.add(
                                0,
                                if (cleanerOn) {
                                    "$timestamp - Cleaning Started"
                                } else {
                                    "$timestamp - Cleaning Stopped"
                                }
                            )
                            // Update cleaner status in Firebase
                            CoroutineScope(Dispatchers.IO).launch {
                                repository.updateCleanerStatus(cleanerOn)
                            }
                        },
                        repository = repository
                    )

                    AppTab.Camera -> CameraScreen()

                    AppTab.Consumption -> HistoryScreen(
                        title = "Power Consumption History",
                        icon = Icons.Rounded.Bolt,
                        accentColor = SolarBlue,
                        repository = repository,
                        dataType = HistoryDataType.CONSUMPTION
                    )

                    AppTab.Harvest -> HistoryScreen(
                        title = "Harvested Power History",
                        icon = Icons.Rounded.WbSunny,
                        accentColor = SolarSun,
                        repository = repository,
                        dataType = HistoryDataType.HARVEST
                    )

                    AppTab.Cleaning -> HistoryScreen(
                        title = "Solar Panel Cleaning History",
                        icon = Icons.Rounded.Autorenew,
                        accentColor = SolarGreen,
                        repository = repository,
                        dataType = HistoryDataType.CLEANING,
                        cleaningHistoryItems = cleaningHistory
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    cleanerOn: Boolean,
    onToggleCleaner: () -> Unit,
    repository: FirebaseRepository
) {
    val cleanerButtonColor by animateColorAsState(
        targetValue = if (cleanerOn) SolarGreen else MaterialTheme.colorScheme.primary,
        label = "ButtonColor"
    )

    var currentSolarData by remember { mutableStateOf<SolarData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var connectionStatus by remember { mutableStateOf("Waiting for data...") }
    var isConnected by remember { mutableStateOf(false) }

    val consumptionData = remember { mutableStateListOf<Float>() }
    val harvestData = remember { mutableStateListOf<Float>() }
    val timeLabels = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        if (consumptionData.isEmpty()) {
            try {
                val history = repository.getHistoricalData(12)
                if (history.isNotEmpty()) {
                    consumptionData.clear()
                    harvestData.clear()
                    timeLabels.clear()

                    history.forEach { data ->
                        consumptionData.add((data.panel1Consumption + data.panel2Consumption).toFloat())
                        harvestData.add((data.panel1Harvest + data.panel2Harvest).toFloat())
                        timeLabels.add(formatTimestamp(data.timestamp))
                    }
                    connectionStatus = "Connected to Firebase"
                    isConnected = true
                } else {
                    connectionStatus = "No data available in Firebase"
                    isConnected = false
                }
            } catch (e: Exception) {
                connectionStatus = "Error: ${e.message}"
                isConnected = false
            }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        repository.observeSolarData().collect { data ->
            currentSolarData = data

            val totalConsumption = data.panel1Consumption + data.panel2Consumption
            val totalHarvest = data.panel1Harvest + data.panel2Harvest

            consumptionData.add(totalConsumption.toFloat())
            harvestData.add(totalHarvest.toFloat())
            timeLabels.add(formatTimestamp(data.timestamp))

            if (consumptionData.size > 12) {
                consumptionData.removeAt(0)
                harvestData.removeAt(0)
                timeLabels.removeAt(0)
            }

            connectionStatus = "Live data from Firebase"
            isConnected = true
            isLoading = false
        }
    }

    ScreenColumn {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (isConnected && currentSolarData != null) SolarGreen else Color.Red, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.bodySmall,
                color = if (isConnected && currentSolarData != null) SolarGreen else Color.Red
            )
        }

        Text(
            text = "Welcome Back,",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Solar Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(20.dp))

        CardContainer {
            Text(
                text = "Live Power Data",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consumption vs Harvest (W)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(SolarOrange, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Consumption", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(SolarBlue, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Harvest", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading || consumptionData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isLoading) "Loading data..." else "No data available",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LineGraph(
                    consumptionData = consumptionData,
                    harvestData = harvestData,
                    timeLabels = timeLabels,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentSolarData != null) {
            PanelCard(
                panelName = "Solar Panel 1",
                consumption = "${currentSolarData!!.panel1Consumption} W",
                harvested = "${currentSolarData!!.panel1Harvest} W"
            )

            PanelCard(
                panelName = "Solar Panel 2",
                consumption = "${currentSolarData!!.panel2Consumption} W",
                harvested = "${currentSolarData!!.panel2Harvest} W"
            )

            CardContainer {
                Text(
                    text = "System Summary",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Power", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "${currentSolarData!!.panel1Consumption + currentSolarData!!.panel2Consumption} W",
                            style = MaterialTheme.typography.titleLarge,
                            color = SolarOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text("Total Harvest", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "${currentSolarData!!.panel1Harvest + currentSolarData!!.panel2Harvest} W",
                            style = MaterialTheme.typography.titleLarge,
                            color = SolarBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text("Efficiency", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = String.format("%.1f%%",
                                if (currentSolarData!!.panel1Harvest + currentSolarData!!.panel2Harvest > 0) {
                                    ((currentSolarData!!.panel1Consumption + currentSolarData!!.panel2Consumption).toFloat() /
                                            (currentSolarData!!.panel1Harvest + currentSolarData!!.panel2Harvest).toFloat()) * 100
                                } else 0f
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            color = SolarGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            CardContainer {
                Text(
                    text = "Waiting for data...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CardContainer {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cleaner Control",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (cleanerOn) "Automatic cleaning active" else "System on standby",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = cleanerOn,
                    onCheckedChange = {
                        onToggleCleaner()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = SolarGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    onToggleCleaner()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cleanerButtonColor,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Autorenew,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (cleanerOn) "Stop Cleaning Cycle" else "Start Cleaning Cycle",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LineGraph(
    consumptionData: List<Float>,
    harvestData: List<Float>,
    timeLabels: List<String>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val graphWidth = width - (padding * 2)
        val graphHeight = height - (padding * 2)

        val maxValue = max(
            consumptionData.maxOrNull() ?: 100f,
            harvestData.maxOrNull() ?: 100f
        ) * 1.1f

        val minValue = 0f

        val gridColor = Color.Gray.copy(alpha = 0.2f)
        for (i in 0..4) {
            val y = padding + (graphHeight * (1f - i / 4f))
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )

            val value = minValue + (maxValue - minValue) * (i / 4f)
            drawContext.canvas.nativeCanvas.apply {
                val textPaint = android.graphics.Paint().apply {
                    color = Color.Gray.toArgb()
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.RIGHT
                    isAntiAlias = true
                }
                drawText(
                    "${value.toInt()}W",
                    padding - 8f,
                    y + 8f,
                    textPaint
                )
            }
        }

        fun drawLine(data: List<Float>, color: Color) {
            if (data.size < 2) return

            val path = Path()
            val step = graphWidth / (data.size - 1)

            data.forEachIndexed { index, value ->
                val x = padding + (index * step)
                val normalizedValue = (value - minValue) / (maxValue - minValue)
                val y = padding + graphHeight * (1f - normalizedValue)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )

            data.forEachIndexed { index, value ->
                val x = padding + (index * step)
                val normalizedValue = (value - minValue) / (maxValue - minValue)
                val y = padding + graphHeight * (1f - normalizedValue)

                drawCircle(
                    color = color,
                    radius = 4f,
                    center = Offset(x, y)
                )
            }
        }

        drawLine(consumptionData, SolarOrange)
        drawLine(harvestData, SolarBlue)

        if (timeLabels.isNotEmpty()) {
            val step = graphWidth / (timeLabels.size - 1)
            timeLabels.forEachIndexed { index, label ->
                val x = padding + (index * step)
                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        color = Color.Gray.toArgb()
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                    drawText(
                        label,
                        x,
                        height - 8f,
                        textPaint
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraScreen() {
    var ipAddress by rememberSaveable { mutableStateOf("") }
    var isConnecting by remember { mutableStateOf(false) }
    var connectionMessage by remember { mutableStateOf("") }

    ScreenColumn {
        Text(
            text = "Live Camera",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Connect to ESP32-CAM for live streaming",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(18.dp))

        CardContainer {
            OutlinedTextField(
                value = ipAddress,
                onValueChange = {
                    ipAddress = it
                    connectionMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("ESP-CAM IP Address") },
                placeholder = { Text("Enter ESP-CAM IP Address") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (connectionMessage.isNotEmpty()) {
                Text(
                    text = connectionMessage,
                    color = if (connectionMessage.contains("Connected")) SolarGreen else Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (ipAddress.isNotBlank()) {
                        isConnecting = true
                        connectionMessage = "Connecting to $ipAddress..."
                        CoroutineScope(Dispatchers.Main).launch {
                            kotlinx.coroutines.delay(2000)
                            isConnecting = false
                            connectionMessage = if (ipAddress.matches(Regex("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))) {
                                "Connected to ESP32-CAM at $ipAddress"
                            } else {
                                "Invalid IP address format"
                            }
                        }
                    } else {
                        connectionMessage = "Please enter an IP address"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = !isConnecting
            ) {
                if (isConnecting) {
                    Text("Connecting...", fontWeight = FontWeight.Bold)
                } else {
                    Text("Connect Camera", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.Videocam,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .shadow(12.dp, RoundedCornerShape(28.dp), ambientColor = Color.Black.copy(alpha = 0.5f), spotColor = Color.Black.copy(alpha = 0.5f), clip = false),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.Videocam,
                        contentDescription = null,
                        tint = if (connectionMessage.contains("Connected")) SolarGreen else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (connectionMessage.contains("Connected")) "Live Stream Active" else "Live Stream Preview",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (connectionMessage.contains("Connected")) SolarGreen else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (connectionMessage.isNotEmpty()) connectionMessage else "Connect to view feed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        if (ipAddress.isNotBlank() && connectionMessage.contains("Connected")) {
            Spacer(modifier = Modifier.height(14.dp))
            CardContainer {
                Text(
                    text = "Stream URL",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "http://${ipAddress.trim()}:81/stream",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click the URL to open in browser or use for streaming",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

enum class HistoryDataType {
    CONSUMPTION,
    HARVEST,
    CLEANING
}

@Composable
private fun HistoryScreen(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    repository: FirebaseRepository,
    dataType: HistoryDataType,
    cleaningHistoryItems: List<String> = emptyList()
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var items by remember { mutableStateOf<List<String>>(emptyList()) }
    var connectionStatus by remember { mutableStateOf("Loading...") }

    LaunchedEffect(dataType) {
        if (dataType == HistoryDataType.CLEANING) {
            items = cleaningHistoryItems.ifEmpty { listOf("No cleaning history available") }
            connectionStatus = "Cleaning history loaded"
            isLoading = false
        } else {
            try {
                val history = repository.getHistoricalData(20)

                items = when (dataType) {
                    HistoryDataType.CONSUMPTION -> {
                        if (history.isNotEmpty()) {
                            history.map { data ->
                                val total = data.panel1Consumption + data.panel2Consumption
                                "${formatTimestamp(data.timestamp)} - Total: $total W (P1: ${data.panel1Consumption}W, P2: ${data.panel2Consumption}W)"
                            }
                        } else {
                            listOf("No consumption data available")
                        }
                    }
                    HistoryDataType.HARVEST -> {
                        if (history.isNotEmpty()) {
                            history.map { data ->
                                val total = data.panel1Harvest + data.panel2Harvest
                                "${formatTimestamp(data.timestamp)} - Total: $total W (P1: ${data.panel1Harvest}W, P2: ${data.panel2Harvest}W)"
                            }
                        } else {
                            listOf("No harvest data available")
                        }
                    }
                    else -> emptyList()
                }

                connectionStatus = if (items.isNotEmpty() && items.first() != "No consumption data available" && items.first() != "No harvest data available") {
                    "Loaded from Firebase"
                } else {
                    "No data available"
                }
            } catch (e: Exception) {
                connectionStatus = "Error: ${e.message}"
                items = when (dataType) {
                    HistoryDataType.CONSUMPTION -> listOf("Error loading consumption data")
                    HistoryDataType.HARVEST -> listOf("Error loading harvest data")
                    else -> emptyList()
                }
            }
            isLoading = false
        }
    }

    val filteredItems = remember(searchQuery, items) {
        if (searchQuery.isBlank()) {
            items
        } else {
            items.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            when {
                                connectionStatus.contains("Firebase") -> SolarGreen
                                connectionStatus.contains("Error") -> Color.Red
                                else -> Color.Gray
                            },
                            CircleShape
                        )
                )
            }
            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search history...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading history...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (filteredItems.isEmpty() || filteredItems.all { it.contains("No data") || it.contains("Error") }) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filteredItems.firstOrNull() ?: "No matching records found.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredItems) { item ->
                HistoryCard(item = item, icon = icon, accentColor = accentColor)
            }
        }
    }
}

@Composable
private fun HistoryCard(item: String, icon: ImageVector, accentColor: Color) {
    CardContainer {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon = icon, color = accentColor)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun CardContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.5f),
                clip = false
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
private fun ScreenColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        content = content
    )
}

@Composable
private fun IconBadge(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun SolarGlideLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.solar_glide),
        contentDescription = "SolarGlide logo",
        modifier = Modifier
            .then(modifier),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun PanelCard(
    panelName: String,
    consumption: String,
    harvested: String
) {
    CardContainer {
        Text(
            text = panelName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Bolt, null, tint = SolarOrange, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Power Consumption: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(consumption, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.WbSunny, null, tint = SolarBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Energy Harvested: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(harvested, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

private fun formatTimestamp(timestamp: Long): String {
    val time = LocalTime.ofSecondOfDay(timestamp / 1000 % 86400)
    return String.format("%d:%02d", time.hour, time.minute)
}

private enum class AppTab(val label: String, val icon: ImageVector) {
    Dashboard("Home", Icons.Rounded.Home),
    Camera("Camera", Icons.Rounded.Videocam),
    Consumption("Usage", Icons.Rounded.Bolt),
    Harvest("Harvest", Icons.Rounded.WbSunny),
    Cleaning("Cleaning", Icons.Rounded.Autorenew)
}

@Preview(showBackground = true)
@Composable
private fun SolarGlidePreview() {
    SolarCleanerTheme {
        SolarGlideApp()
    }
}