package com.ayerdi.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayerdi.lab7.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Pantalla1(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla1(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val notifications = remember { generateFakeNotifications() }
    var selectedFilter by remember { mutableStateOf<NotificationType?>(null) }

    val filteredNotifications = if (selectedFilter != null) {
        notifications.filter { it.type == selectedFilter }
    } else {
        notifications
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primary)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = colors.onPrimary
            )
            Text(
                text = "Notificaciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = colors.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Tipos de notificaciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colors.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            NotificationFilters(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = if (selectedFilter == filter) null else filter
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredNotifications) { notification ->
                        NotificationItem(notification = notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationFilters(
    selectedFilter: NotificationType?,
    onFilterSelected: (NotificationType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilterChip(
            type = NotificationType.GENERAL,
            isSelected = selectedFilter == NotificationType.GENERAL,
            onClick = { onFilterSelected(NotificationType.GENERAL) }
        )

        FilterChip(
            type = NotificationType.NEW_MEETING,
            isSelected = selectedFilter == NotificationType.NEW_MEETING,
            onClick = { onFilterSelected(NotificationType.NEW_MEETING) }
        )
    }
}

@Composable
fun FilterChip(
    type: NotificationType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val chipText = when (type) {
        NotificationType.GENERAL -> "Informativas"
        NotificationType.NEW_MEETING -> "Capacitaciones"
    }

    val backgroundColor = if (isSelected) colors.tertiaryContainer else colors.surface
    val contentColor = if (isSelected) colors.onTertiaryContainer else colors.onSurface
    val borderColor = if (isSelected) colors.tertiaryContainer else colors.outline

    Row(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = chipText,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val (backgroundColor, iconColor, icon) = getNotificationStyle(notification.type, colors)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = notification.sendAt,
                    fontSize = 12.sp,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = notification.body,
                fontSize = 14.sp,
                color = colors.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun getNotificationStyle(
    type: NotificationType,
    colors: ColorScheme
): Triple<androidx.compose.ui.graphics.Color, androidx.compose.ui.graphics.Color, ImageVector> {
    return when (type) {
        NotificationType.GENERAL -> Triple(
            colors.primaryContainer,
            colors.onPrimaryContainer,
            Icons.Default.Notifications
        )
        NotificationType.NEW_MEETING -> Triple(
            colors.tertiaryContainer,
            colors.onTertiaryContainer,
            Icons.Default.DateRange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationCenter() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Pantalla1(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}