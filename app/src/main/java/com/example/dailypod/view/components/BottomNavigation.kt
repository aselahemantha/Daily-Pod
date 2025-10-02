package com.example.dailypod.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dailypod.R
import com.example.dailypod.view.util.scale.scaleElevation
import com.example.dailypod.view.util.scale.scaleHeight
import com.example.dailypod.view.util.scale.scaleRadius
import com.example.dailypod.view.util.scale.scaleWidth

@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs =
        listOf(
            BottomNavTab(
                "today",
                "Today",
                painterResource(R.drawable.home_icon),
            ),
            BottomNavTab(
                "stats",
                "Stats",
                painterResource(R.drawable.stat_icon),
            ),
            BottomNavTab(
                "add",
                "Add",
                painterResource(R.drawable.add_icon),
                isSpecial = true,
            ),
            BottomNavTab(
                "calendar",
                "Calendar",
                painterResource(R.drawable.calender_icon),
            ),
            BottomNavTab(
                "settings",
                "Settings",
                painterResource(R.drawable.setting_icon),
            ),
        )

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues()),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8f.scaleElevation(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16f.scaleWidth(), vertical = 8f.scaleHeight()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                BottomNavItem(
                    tab = tab,
                    isSelected = currentRoute == tab.route,
                    onClick = { onNavigate(tab.route) },
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun BottomNavItem(
    tab: BottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (tab.isSpecial) {
            Box(
                modifier =
                    Modifier
                        .size(56f.scaleWidth())
                        .offset(y = (-8).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = tab.icon,
                    contentDescription = tab.label,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24f.scaleWidth()),
                )
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .size(48f.scaleWidth())
                        .clip(RoundedCornerShape(12f.scaleRadius()))
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.Transparent
                            },
                        ).clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = tab.icon,
                    contentDescription = tab.label,
                    tint =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    modifier = Modifier.size(24f.scaleWidth()),
                )
            }

            Spacer(modifier = Modifier.height(4f.scaleHeight()))

            Text(
                text = tab.label,
                style = MaterialTheme.typography.labelSmall,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )
        }
    }
}

private data class BottomNavTab(
    val route: String,
    val label: String,
    val icon: Painter,
    val isSpecial: Boolean = false,
)
