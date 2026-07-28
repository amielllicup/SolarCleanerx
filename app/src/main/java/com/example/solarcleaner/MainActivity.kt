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
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
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
    val date: String,
    val panel: String,
    val consumption: String,
    val harvest: String
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
    
    // Observe Firebase data
    val cleanerOn by repository.getCleanerStatus().collectAsState(initial = false)
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
                        onToggleCleaner = { repository.toggleCleaner(!cleanerOn) }
                    )

                    AppTab.Camera -> CameraScreen()
                    
                    AppTab.Records -> {
                        val dailyRecords = fbHistory.flatMap { fbRecord ->
                            val dateStr = sdf.format(Date(fbRecord.timestamp))
                            listOf(
                                DailyRecord(dateStr, "Panel 1", "${fbRecord.panel1Consumption} W", "${fbRecord.panel1Harvest} W"),
                                DailyRecord(dateStr, "Panel 2", "${fbRecord.panel2Consumption} W", "${fbRecord.panel2Harvest} W")
                            )
                        }
                        DailyRecordsScreen(dailyRecords)
                    }
                    
                    AppTab.Cleaning -> {
                        val cleaningRecords = fbCleaningHistory.map { fbRecord ->
                            CleaningRecord(
                                date = sdf.format(Date(fbRecord.timestamp)),
                                time = timeSdf.format(Date(fbRecord.timestamp)),
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
private fun DashboardScreen(cleanerOn: Boolean, solarData: SolarLiveData, onToggleCleaner: () -> Unit) {
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

        LivePowerChart(solarData)

        PanelCard(
            panelName = "Solar Panel 1",
            consumption = "${solarData.panel1Consumption} W",
            harvested = "${solarData.panel1Harvest} W"
        )
        
        PanelCard(
            panelName = "Solar Panel 2",
            consumption = "${solarData.panel2Consumption} W",
            harvested = "${solarData.panel2Harvest} W"
        )

        Spacer(modifier = Modifier.height(4.dp))
        
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
private fun LivePowerChart(liveData: SolarLiveData) {
    val modelProducer = remember { CartesianChartModelProducer() }
    
    var s1ConsSamples by remember { mutableStateOf(List(12) { 0 }) }
    var s1HarvSamples by remember { mutableStateOf(List(12) { 0 }) }
    var s2ConsSamples by remember { mutableStateOf(List(12) { 0 }) }
    var s2HarvSamples by remember { mutableStateOf(List(12) { 0 }) }

    LaunchedEffect(liveData) {
        s1ConsSamples = s1ConsSamples.drop(1) + liveData.panel1Consumption
        s1HarvSamples = s1HarvSamples.drop(1) + liveData.panel1Harvest
        s2ConsSamples = s2ConsSamples.drop(1) + liveData.panel2Consumption
        s2HarvSamples = s2HarvSamples.drop(1) + liveData.panel2Harvest
        
        modelProducer.runTransaction {
            lineSeries {
                series(s1ConsSamples)
                series(s1HarvSamples)
                series(s2ConsSamples)
                series(s2HarvSamples)
            }
        }
    }

    val currentTotalCons = s1ConsSamples.last() + s2ConsSamples.last()
    val currentTotalHarv = s1HarvSamples.last() + s2HarvSamples.last()

    CardContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Live Power Monitoring",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "System Consumption vs Harvest (W)",
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
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                LegendItem(color = SolarOrange, label = "S1 Cons")
                LegendItem(color = SolarBlue, label = "S1 Harv")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                LegendItem(color = SolarGold, label = "S2 Cons")
                LegendItem(color = SolarGreen, label = "S2 Harv")
            }
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
                            fill = LineCartesianLayer.LineFill.single(fill(SolarGold)),
                            areaFill = LineCartesianLayer.AreaFill.single(fill(SolarGold.copy(alpha = 0.15f))),
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.point(rememberShapeComponent(fill(SolarGold), CorneredShape.Pill), size = 6.dp)
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
                    guideline = rememberLineComponent(
                        fill = fill(SolarOutline.copy(alpha = 0.3f)),
                        shape = dashedShape()
                    )
                )
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChartStatItem(
                icon = Icons.Rounded.Bolt,
                color = SolarOrange,
                value = "$currentTotalCons W",
                label = "Total Power"
            )
            ChartStatItem(
                icon = Icons.Rounded.WbSunny,
                color = SolarBlue,
                value = "$currentTotalHarv W",
                label = "Total Harvest"
            )
            val efficiency = if (currentTotalCons > 0) (currentTotalHarv * 100 / currentTotalCons) else 0
            ChartStatItem(
                icon = Icons.Rounded.EnergySavingsLeaf,
                color = SolarGreen,
                value = "$efficiency%",
                label = "Efficiency"
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

    ScreenColumn {
        Text(
            text = "Live Camera",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Frontend preview for future ESP32-CAM streaming.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(18.dp))

        CardContainer {
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
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
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("CAM", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect Camera")
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Live Stream Preview",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Connect to view feed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        if (ipAddress.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))
            CardContainer {
                Text(
                    text = "Sample stream URL",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "http://${ipAddress.trim()}:81/stream",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyRecordsScreen(allRecords: List<DailyRecord>) {
    val panels = listOf("All Panels", "Panel 1", "Panel 2")
    var selectedPanel by rememberSaveable { mutableStateOf(panels[0]) }
    var expanded by remember { mutableStateOf(false) }
    var selectedDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }

    val filteredRecords = remember(allRecords, selectedPanel, selectedDateMillis) {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val dateString = selectedDateMillis?.let { sdf.format(Date(it)) }
        
        allRecords.filter { record ->
            val panelMatch = selectedPanel == "All Panels" || record.panel == selectedPanel
            val dateMatch = dateString == null || record.date == dateString
            panelMatch && dateMatch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Daily Records",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Consumption and Harvest logs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).clickable { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedPanel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Panel") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.45f)
                ) {
                    panels.forEach { panel ->
                        DropdownMenuItem(
                            text = { Text(panel) },
                            onClick = {
                                selectedPanel = panel
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            DatePickerField(
                selectedDate = selectedDateMillis,
                onDateSelected = { selectedDateMillis = it },
                label = "Date",
                modifier = Modifier.weight(1.2f)
            )
        }

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
                    Text("Panel", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Usage", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Harvest", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // Scrollable Data Rows
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Fills the remaining 420.dp space
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
                            Text(record.panel, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            Text(record.consumption, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = SolarOrange, fontWeight = FontWeight.Bold)
                            Text(record.harvest, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = SolarBlue, fontWeight = FontWeight.Bold)
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

    Box(modifier = modifier.clickable {
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
