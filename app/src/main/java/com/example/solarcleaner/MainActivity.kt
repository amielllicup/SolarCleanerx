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
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SolarPower
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EnergySavingsLeaf
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import java.net.HttpURLConnection
import java.net.URL
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.example.solarcleaner.R
import com.example.solarcleaner.data.FirebaseRepository
import com.example.solarcleaner.data.SolarLiveData
import com.example.solarcleaner.data.FirebaseHistoryRecord
import com.example.solarcleaner.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class DailyRecord(
    val time: String,
    val consumption: String,
    val s1Harvest: String,
    val s2Harvest: String
)

private data class CleaningRecord(
    val date: String,
    val time: String,
    val action: String,
    val status: String
)

private const val LoginEmail = "admin@solarglide.com"
private const val LoginPassword = "Admin@1234"

class MainActivity : ComponentActivity() {
    private val repository by lazy { FirebaseRepository() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SolarCleanerTheme {
                SolarGlideApp(repository)
            }
        }
    }
}

@Composable
private fun SolarGlideApp(repository: FirebaseRepository) {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Crossfade(targetState = isLoggedIn, animationSpec = tween(800)) { loggedIn ->
            if (loggedIn) {
                MainApp(repository, onLogout = { isLoggedIn = false })
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
private fun MainApp(repository: FirebaseRepository, onLogout: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Dashboard) }
    
    // Pure Firebase Sync - No local latches or enforcement logic
    val cleanerOn by repository.getCleanerStatus().collectAsState(initial = false)
    
    // Observe Firebase data
    val solarLiveData by repository.getSolarLiveData().collectAsState(initial = SolarLiveData())
    val fbHistory by repository.getHistory().collectAsState(initial = emptyList())
    val fbCleaningHistory by repository.getCleaningHistory().collectAsState(initial = emptyList())

    val sdf = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()) }
    val timeSdf = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

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
                        solarData = solarLiveData ?: SolarLiveData(),
                        history = fbHistory,
                        onToggleCleaner = { repository.toggleCleaner(!cleanerOn) }
                    )

                    AppTab.Camera -> CameraScreen()
                    
                    AppTab.Records -> {
                        HistoryRecordsScreen(fbHistory)
                    }
                    
                    AppTab.Cleaning -> {
                        val cleaningRecords = fbCleaningHistory.map { fbRecord ->
                            val ts = fbRecord.getSafeTimestamp()
                            CleaningRecord(
                                date = sdf.format(Date(ts)),
                                time = timeSdf.format(Date(ts)),
                                action = fbRecord.action,
                                status = fbRecord.status
                            )
                        }
                        CleaningHistoryScreen(cleaningRecords)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    cleanerOn: Boolean, 
    solarData: SolarLiveData, 
    history: List<FirebaseHistoryRecord>,
    onToggleCleaner: () -> Unit
) {
    val cleanerButtonColor by animateColorAsState(
        targetValue = if (cleanerOn) SolarGreen else MaterialTheme.colorScheme.primary,
        label = "ButtonColor"
    )

    ScreenColumn {
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

        LivePowerChart(history, solarData)

        PanelCard(
            panelName = "Solar Panel 1",
            harvested = String.format(Locale.getDefault(), "%.1f V", solarData.solar1Harvest)
        )
        
        PanelCard(
            panelName = "Solar Panel 2",
            harvested = String.format(Locale.getDefault(), "%.2f V", solarData.solar2Harvest)
        )

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
                    onCheckedChange = { onToggleCleaner() },
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
                onClick = onToggleCleaner,
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
private fun LivePowerChart(history: List<FirebaseHistoryRecord>, liveData: SolarLiveData) {
    val modelProducer = remember { CartesianChartModelProducer() }
    
    // Filter history for latest active day recorded in database (optimized)
    val todayRecords = remember(history) {
        if (history.isEmpty()) return@remember emptyList()
        
        // Use the most recent record's date as the boundary
        val latestTs = history.firstOrNull()?.getSafeTimestamp() ?: return@remember emptyList()
        val latestCal = Calendar.getInstance().apply { timeInMillis = latestTs }
        
        // Calculate start and end of that day in millis
        latestCal.set(Calendar.HOUR_OF_DAY, 0)
        latestCal.set(Calendar.MINUTE, 0)
        latestCal.set(Calendar.SECOND, 0)
        latestCal.set(Calendar.MILLISECOND, 0)
        val startOfDay = latestCal.timeInMillis
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000)
        
        // Fast numeric filter (no Calendar creation inside the loop)
        history.filter { 
            val ts = it.getSafeTimestamp()
            ts in startOfDay until endOfDay
        }.sortedBy { it.getSafeTimestamp() }
    }

    LaunchedEffect(todayRecords) {
        if (todayRecords.isNotEmpty()) {
            modelProducer.runTransaction {
                lineSeries {
                    series(
                        x = todayRecords.map { it.getSafeTimestamp().toFloat() },
                        y = todayRecords.map { it.getSafeCons().toFloat() }
                    )
                    series(
                        x = todayRecords.map { it.getSafeTimestamp().toFloat() },
                        y = todayRecords.map { it.getSafeS1().toFloat() }
                    )
                    series(
                        x = todayRecords.map { it.getSafeTimestamp().toFloat() },
                        y = todayRecords.map { it.getSafeS2().toFloat() }
                    )
                }
            }
        }
    }

    CardContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "System Monitoring",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                val displayDate = remember(todayRecords) {
                    val ts = todayRecords.lastOrNull()?.getSafeTimestamp() ?: System.currentTimeMillis()
                    SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date(ts))
                }
                Text(
                    text = displayDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(SolarGreen, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            LegendItem(color = SolarOrange, label = "Consumption")
            LegendItem(color = SolarBlue, label = "S1 Harvest")
            LegendItem(color = SolarGreen, label = "S2 Harvest")
        }

        Spacer(modifier = Modifier.height(18.dp))
        
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(SolarOrange)),
                            areaFill = LineCartesianLayer.AreaFill.single(fill(SolarOrange.copy(alpha = 0.15f))),
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.point(rememberShapeComponent(fill(SolarOrange), CorneredShape.Pill), size = 6.dp)
                            )
                        ),
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(SolarBlue)),
                            areaFill = LineCartesianLayer.AreaFill.single(fill(SolarBlue.copy(alpha = 0.15f))),
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.point(rememberShapeComponent(fill(SolarBlue), CorneredShape.Pill), size = 6.dp)
                            )
                        ),
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(SolarGreen)),
                            areaFill = LineCartesianLayer.AreaFill.single(fill(SolarGreen.copy(alpha = 0.15f))),
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.point(rememberShapeComponent(fill(SolarGreen), CorneredShape.Pill), size = 6.dp)
                            )
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    guideline = rememberLineComponent(
                        fill = fill(SolarOutline.copy(alpha = 0.3f)),
                        shape = dashedShape()
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, value, _ ->
                        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                        sdf.format(Date(value.toLong()))
                    },
                    guideline = rememberLineComponent(
                        fill = fill(SolarOutline.copy(alpha = 0.3f)),
                        shape = dashedShape()
                    )
                )
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            scrollState = rememberVicoScrollState(scrollEnabled = false),
            zoomState = rememberVicoZoomState(zoomEnabled = false)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val latestSnapshot = todayRecords.lastOrNull() ?: FirebaseHistoryRecord()

            ChartStatItem(
                icon = Icons.Rounded.Bolt,
                color = SolarOrange,
                value = String.format(Locale.getDefault(), "%.1f%%", latestSnapshot.getSafeCons()),
                label = "Consumption"
            )
            ChartStatItem(
                icon = Icons.Rounded.LightMode,
                color = SolarBlue,
                value = String.format(Locale.getDefault(), "%.1fV", latestSnapshot.getSafeS1()),
                label = "S1 Harvest"
            )
            ChartStatItem(
                icon = Icons.Rounded.SolarPower,
                color = SolarGreen,
                value = String.format(Locale.getDefault(), "%.2fV", latestSnapshot.getSafeS2()),
                label = "S2 Harvest"
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ChartStatItem(icon: ImageVector, color: Color, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CameraScreen() {
    var ipAddress by rememberSaveable { mutableStateOf("") }
    var isConnecting by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var workingPort by remember { mutableIntStateOf(80) }
    var refreshKey by remember { mutableIntStateOf(0) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Connection Successful", fontWeight = FontWeight.Bold) },
            text = { Text("Camera reached on port $workingPort. Stream is now active.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = SolarCardDark,
            titleContentColor = MaterialTheme.colorScheme.primary,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    ScreenColumn {
        Text(
            text = "Live Camera",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Enter your ESP32-CAM IP address to start streaming.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(18.dp))

        CardContainer {
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { 
                    ipAddress = it 
                    isConnected = false
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("ESP-CAM IP Address") },
                placeholder = { Text("e.g., 192.168.100.33") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (ipAddress.isBlank()) {
                        errorMessage = "Please enter an IP address"
                        return@Button
                    }
                    
                    isConnecting = true
                    errorMessage = null
                    
                    scope.launch {
                        val sanitizedIp = ipAddress.trim()
                            .replace("http://", "")
                            .replace("https://", "")
                            .removeSuffix("/")
                        
                        val portFound = withContext(Dispatchers.IO) {
                            var successfulPort = -1
                            for (port in listOf(80, 81)) { // Try 80 first as it's common for commands
                                try {
                                    val url = URL("http://$sanitizedIp:$port")
                                    val connection = url.openConnection() as HttpURLConnection
                                    connection.connectTimeout = 2000
                                    connection.connect()
                                    if (connection.responseCode != -1) {
                                        successfulPort = port
                                        
                                        // SEQUENTIAL LOGIC: Set resolution BEFORE connecting stream
                                        // This ensures we only use ONE connection at a time
                                        try {
                                            val loResUrl = URL("http://$sanitizedIp:$port/cam-lo.jpg")
                                            val loResConn = loResUrl.openConnection() as HttpURLConnection
                                            loResConn.connectTimeout = 2000
                                            loResConn.connect()
                                            loResConn.responseCode
                                        } catch (e: Exception) {}
                                        
                                        break
                                    }
                                } catch (e: Exception) {
                                    continue
                                }
                            }
                            successfulPort
                        }
                        
                        isConnecting = false
                        if (portFound != -1) {
                            workingPort = portFound
                            isConnected = true
                            showSuccessDialog = true
                        } else {
                            errorMessage = "Could not reach camera. Check IP and network."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isConnected) SolarGreen else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                enabled = !isConnecting
            ) {
                if (isConnecting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = if (isConnected) Icons.Rounded.EnergySavingsLeaf else Icons.Rounded.Videocam, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(if (isConnected) "Connected" else "Connect Camera", fontWeight = FontWeight.Bold)
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
                if (isConnected) {
                    val sanitizedIpForUrl = ipAddress.trim()
                        .replace("http://", "")
                        .replace("https://", "")
                        .removeSuffix("/")
                    
                    key(refreshKey) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    settings.loadWithOverviewMode = true
                                    settings.useWideViewPort = true
                                    settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                                    settings.builtInZoomControls = false
                                    settings.displayZoomControls = false
                                    webViewClient = WebViewClient()
                                    
                                    // Set background color to black to remove white bars
                                    setBackgroundColor(android.graphics.Color.BLACK)
                                    
                                    // Load URL directly - confirmed working path
                                    loadUrl("http://$sanitizedIpForUrl:$workingPort/stream")
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            onRelease = { webView ->
                                webView.stopLoading()
                                webView.destroy()
                            }
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.Videocam,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Stream Active",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Connect to your ESP32-CAM",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        if (isConnected) {
            Spacer(modifier = Modifier.height(16.dp))
            CardContainer {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Active Stream",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val sanitizedUrl = ipAddress.trim()
                                .replace("http://", "")
                                .replace("https://", "")
                                .removeSuffix("/")
                            Text(
                                text = "http://$sanitizedUrl:$workingPort/stream",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { refreshKey++ },
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Autorenew,
                                contentDescription = "Refresh",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Resolution Control",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Low" to "lo", "Mid" to "mid", "High" to "hi").forEach { (label, suffix) ->
                            Button(
                                onClick = {
                                    scope.launch {
                                        val sanitized = ipAddress.trim()
                                            .replace("http://", "")
                                            .replace("https://", "")
                                            .removeSuffix("/")
                                        withContext(Dispatchers.IO) {
                                            try {
                                                val url = URL("http://$sanitized:$workingPort/cam-$suffix.jpg")
                                                val conn = url.openConnection() as HttpURLConnection
                                                conn.connectTimeout = 2000
                                                conn.connect()
                                                conn.responseCode
                                            } catch (e: Exception) {}
                                        }
                                        refreshKey++
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(label, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryRecordsScreen(history: List<FirebaseHistoryRecord>) {
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    val timeSdf = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val filteredRecords = remember(history, selectedDateMillis) {
        if (selectedDateMillis == null) return@remember history
        
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis!! }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000)
        
        history.filter { 
            val ts = it.getSafeTimestamp()
            ts in startOfDay until endOfDay
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Text(
            text = "History Records",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = if (selectedDateMillis == null) "Showing All History" else "Consumption and Harvest logs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        DatePickerField(
            selectedDate = selectedDateMillis,
            onDateSelected = { selectedDateMillis = it },
            label = "Filter by Date",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        // Data Log Table
        Column(modifier = Modifier.fillMaxWidth().height(420.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("Time", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("Cons", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("S1", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("S2", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                if (filteredRecords.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(text = "No records found.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(filteredRecords.size) { index ->
                        val record = filteredRecords[index]
                        val isLast = index == filteredRecords.size - 1
                        
                        // Dynamic formatting
                        val ts = record.getSafeTimestamp()
                        val isRealTime = ts > 500000000000L // Threshold for dates after 1985
                        val dateLabel = if (isRealTime) {
                            SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(ts))
                        } else if (ts > 0) "Offset" else "System"
                        
                        val timeLabel = if (isRealTime) {
                            timeSdf.format(Date(ts))
                        } else if (ts > 0) "Log" else "Uptime"

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = if (isLast) RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp) else RoundedCornerShape(0.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(dateLabel, modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.bodySmall)
                                Text(timeLabel, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                                Text(String.format(Locale.getDefault(), "%.1f%%", record.getSafeCons()), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = SolarOrange, fontWeight = FontWeight.Bold)
                                Text(String.format(Locale.getDefault(), "%.1fV", record.getSafeS1()), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = SolarBlue, fontWeight = FontWeight.Bold)
                                Text(String.format(Locale.getDefault(), "%.1fV", record.getSafeS2()), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = SolarBlue, fontWeight = FontWeight.Bold)
                            }
                            if (!isLast) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleaningHistoryScreen(records: List<CleaningRecord>) {
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    
    val filteredRecords = remember(records, selectedDateMillis) {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val dateString = selectedDateMillis?.let { sdf.format(Date(it)) }
        
        if (dateString == null) records
        else records.filter { it.date == dateString }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Cleaning History",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Logs of automated cleaning cycles",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        DatePickerField(
            selectedDate = selectedDateMillis,
            onDateSelected = { selectedDateMillis = it },
            label = "Filter by Date",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        // Fixed Height Table Container
        Column(modifier = Modifier.fillMaxWidth().height(420.dp)) {
            // Table Header (Fixed)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Time", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Action", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Status", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // Scrollable Data Rows
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredRecords.size) { index ->
                    val record = filteredRecords[index]
                    val isLast = index == filteredRecords.size - 1
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = if (isLast) RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp) else RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(record.date, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall)
                            Text(record.time, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            
                            Text(
                                text = record.action,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (record.action == "Start") SolarGreen else SolarOrange,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = record.status,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (record.status == "Active" || record.status == "Completed") SolarGreen else SolarOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (!isLast) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val formattedDate = remember(selectedDate) {
        if (selectedDate != null) {
            val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            sdf.format(Date(selectedDate))
        } else {
            "All Dates"
        }
    }

    Box(
        modifier = modifier.clickable {
            val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.timeInMillis = it }
        
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val resultCalendar = Calendar.getInstance()
                resultCalendar.set(year, month, dayOfMonth)
                onDateSelected(resultCalendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = { Icon(Icons.Rounded.CalendarMonth, null) },
            trailingIcon = {
                if (selectedDate != null) {
                    IconButton(onClick = { onDateSelected(null) }) {
                        Icon(Icons.Rounded.Close, null)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun CardContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black,
                clip = false
            ),
        colors = CardDefaults.cardColors(containerColor = SolarCardDark),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), content = content)
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
            Icon(Icons.Rounded.WbSunny, null, tint = SolarBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Energy Harvested: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(harvested, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

private enum class AppTab(val label: String, val icon: ImageVector) {
    Dashboard("Home", Icons.Rounded.Home),
    Camera("Camera", Icons.Rounded.Videocam),
    Records("Records", Icons.Rounded.TableChart),
    Cleaning("Cleaning", Icons.Rounded.Autorenew)
}

@Preview(showBackground = true)
@Composable
private fun SolarGlidePreview() {
    SolarCleanerTheme {
        SolarGlideApp(FirebaseRepository())
    }
}
