package com.example.solarcleaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.solarcleaner.ui.theme.SolarBackground
import com.example.solarcleaner.ui.theme.SolarBlue
import com.example.solarcleaner.ui.theme.SolarBlueLight
import com.example.solarcleaner.ui.theme.SolarCleanerTheme
import com.example.solarcleaner.ui.theme.SolarGray
import com.example.solarcleaner.ui.theme.SolarGreen
import com.example.solarcleaner.ui.theme.SolarNavy
import com.example.solarcleaner.ui.theme.SolarOrange
import com.example.solarcleaner.ui.theme.SolarSun

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
        if (isLoggedIn) {
            MainApp(onLogout = { isLoggedIn = false })
        } else {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
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
            .background(SolarBackground)
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
                    .fillMaxWidth()
                    .height(190.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Automatic Solar Panel Cleaning",
                style = MaterialTheme.typography.bodyLarge,
                color = SolarGray
            )
            Spacer(modifier = Modifier.height(24.dp))

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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                            Text(if (passwordVisible) "Hide" else "Show")
                        }
                    }
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
                        if (email.trim() == LoginEmail && password == LoginPassword) {
                            errorMessage = null
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid email or password."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
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
    val cleaningHistory = remember {
        mutableStateListOf(
            "June 4, 2026 - Cleaning Started",
            "June 4, 2026 - Cleaning Stopped",
            "June 3, 2026 - Cleaning Completed"
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SolarGlideLogo(
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("SolarGlide", fontWeight = FontWeight.Bold, color = SolarNavy)
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", fontWeight = FontWeight.Bold)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { NavBadge(tab.iconText, selectedTab == tab) },
                        label = { Text(tab.label) }
                    )
                }
            }
        },
        containerColor = SolarBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.Dashboard -> DashboardScreen(
                    cleanerOn = cleanerOn,
                    onToggleCleaner = {
                        cleanerOn = !cleanerOn
                        cleaningHistory.add(
                            0,
                            if (cleanerOn) {
                                "June 4, 2026 - Cleaning Started"
                            } else {
                                "June 4, 2026 - Cleaning Stopped"
                            }
                        )
                    }
                )

                AppTab.Camera -> CameraScreen()
                AppTab.Consumption -> HistoryScreen(
                    title = "Power Consumption History",
                    iconText = "W",
                    accentColor = SolarBlue,
                    items = listOf(
                        "8:00 AM - 100 W",
                        "10:00 AM - 125 W",
                        "12:00 PM - 140 W",
                        "2:00 PM - 115 W"
                    )
                )

                AppTab.Harvest -> HistoryScreen(
                    title = "Harvested Power History",
                    iconText = "SOL",
                    accentColor = SolarSun,
                    items = listOf(
                        "8:00 AM - 250 W",
                        "10:00 AM - 390 W",
                        "12:00 PM - 480 W",
                        "2:00 PM - 430 W"
                    )
                )

                AppTab.Cleaning -> HistoryScreen(
                    title = "Solar Panel Cleaning History",
                    iconText = "CLN",
                    accentColor = SolarGreen,
                    items = cleaningHistory
                )
            }
        }
    }
}

@Composable
private fun DashboardScreen(cleanerOn: Boolean, onToggleCleaner: () -> Unit) {
    ScreenColumn {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            color = SolarNavy
        )
        Text(
            text = "Monitor panel output and control the automatic cleaner.",
            style = MaterialTheme.typography.bodyMedium,
            color = SolarGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        MetricCard(
            title = "Current Power Consumption",
            value = "120 W",
            iconText = "W",
            iconColor = SolarOrange
        )
        MetricCard(
            title = "Current Power Harvested",
            value = "450 W",
            iconText = "SOL",
            iconColor = SolarBlue
        )
        MetricCard(
            title = "Current Harvest History",
            value = "Today: 3.8 kWh",
            iconText = "kWh",
            iconColor = SolarGreen
        )

        Spacer(modifier = Modifier.height(10.dp))
        CardContainer {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cleaner Controller",
                        style = MaterialTheme.typography.titleLarge,
                        color = SolarNavy
                    )
                    Text(
                        text = "Tap to start or stop cleaning.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SolarGray
                    )
                }
                StatusPill(
                    text = if (cleanerOn) "ON" else "OFF",
                    active = cleanerOn
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onToggleCleaner,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (cleanerOn) SolarGreen else Color(0xFFCBD3DC),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (cleanerOn) "Turn Cleaner Off" else "Turn Cleaner On",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun CameraScreen() {
    var ipAddress by rememberSaveable { mutableStateOf("") }

    ScreenColumn {
        Text(
            text = "Live Camera",
            style = MaterialTheme.typography.headlineSmall,
            color = SolarNavy
        )
        Text(
            text = "Frontend preview for future ESP32-CAM streaming.",
            style = MaterialTheme.typography.bodyMedium,
            color = SolarGray
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
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
                .height(220.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp), clip = false),
            colors = CardDefaults.cardColors(containerColor = SolarNavy),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CAM",
                        style = MaterialTheme.typography.titleLarge,
                        color = SolarBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Live Camera Preview",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
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
                    color = SolarNavy
                )
                Text(
                    text = "http://${ipAddress.trim()}:81/stream",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SolarBlue
                )
            }
        }
    }
}

@Composable
private fun HistoryScreen(
    title: String,
    iconText: String,
    accentColor: Color,
    items: List<String>
) {
    ScreenColumn {
        Text(text = title, style = MaterialTheme.typography.headlineSmall, color = SolarNavy)
        Spacer(modifier = Modifier.height(18.dp))
        items.forEach { item ->
            HistoryCard(item = item, iconText = iconText, accentColor = accentColor)
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    iconText: String,
    iconColor: Color
) {
    CardContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge(text = iconText, color = iconColor)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = SolarGray)
                Text(text = value, style = MaterialTheme.typography.titleLarge, color = SolarNavy)
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun HistoryCard(item: String, iconText: String, accentColor: Color) {
    CardContainer {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(text = iconText, color = accentColor)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = item,
                style = MaterialTheme.typography.bodyLarge,
                color = SolarNavy
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
            .shadow(4.dp, RoundedCornerShape(20.dp), clip = false),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun ScreenColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        content = content
    )
}

@Composable
private fun IconBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NavBadge(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(
                color = if (selected) SolarBlue.copy(alpha = 0.14f) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) SolarBlue else SolarGray,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatusPill(text: String, active: Boolean) {
    Row(
        modifier = Modifier
            .background(
                color = if (active) SolarGreen.copy(alpha = 0.14f) else SolarBlueLight,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (active) "ON" else "OFF",
            color = if (active) SolarGreen else SolarGray,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = if (active) SolarGreen else SolarGray,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun SolarGlideLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.solar_glide),
        contentDescription = "SolarGlide logo",
        modifier = Modifier
            .then(modifier),
        contentScale = ContentScale.Fit
    )
}

private enum class AppTab(val label: String, val iconText: String) {
    Dashboard("Home", "DB"),
    Camera("Camera", "CAM"),
    Consumption("Consumption", "W"),
    Harvest("Harvest", "SOL"),
    Cleaning("Cleaning", "CLN")
}

@Preview(showBackground = true)
@Composable
private fun SolarGlidePreview() {
    SolarCleanerTheme {
        SolarGlideApp()
    }
}
