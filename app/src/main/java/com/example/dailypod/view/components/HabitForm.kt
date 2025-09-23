package com.example.dailypod.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dailypod.data.enums.TargetFrequency

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitForm(
    onSubmit: (HabitFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(colors[0]) }
    var selectedIcon by remember { mutableStateOf(icons[0]) }
    var targetFrequency by remember { mutableStateOf(TargetFrequency.DAILY) }
    var targetCount by remember { mutableStateOf(1) }

    Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = modifier.widthIn(max = 400.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Create New Habit",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )

                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Habit Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    placeholder = { Text("e.g., Morning exercise") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    placeholder = { Text("What does this habit involve?") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    maxLines = 3,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Icon Selection
                Text(
                    text = "Choose an Icon",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(icons) { icon ->
                        Box(
                            modifier =
                                Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedIcon == icon) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        },
                                    ).clickable { selectedIcon = icon },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = icon,
                                fontSize = 24.sp,
                                color =
                                    if (selectedIcon == icon) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Selection
                Text(
                    text = "Choose a Color",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(60.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(colors) { color ->
                        Box(
                            modifier =
                                Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(color)))
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 0.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape,
                                    ).clickable { selectedColor = color },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onSubmit(
                                HabitFormData(
                                    name = name.trim(),
                                    description = description.trim().takeIf { it.isNotEmpty() },
                                    color = selectedColor,
                                    icon = selectedIcon,
                                    targetFrequency = targetFrequency,
                                    targetCount = targetCount,
                                ),
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.trim().isNotEmpty(),
                    ) {
                        Text("Create Habit")
                    }
                }
            }
        }
    }
}

data class HabitFormData(
    val name: String,
    val description: String?,
    val color: String,
    val icon: String,
    val targetFrequency: TargetFrequency,
    val targetCount: Int,
)

private val colors =
    listOf(
        "#3B82F6",
        "#EF4444",
        "#10B981",
        "#F59E0B",
        "#8B5CF6",
        "#EC4899",
        "#06B6D4",
        "#84CC16",
    )

private val icons =
    listOf(
        "💪",
        "📚",
        "🏃",
        "🧘",
        "💧",
        "🌱",
        "🎯",
        "✍️",
        "🍎",
        "💤",
        "🚶",
        "🎵",
        "📖",
        "🧹",
        "🎨",
        "🔋",
    )
