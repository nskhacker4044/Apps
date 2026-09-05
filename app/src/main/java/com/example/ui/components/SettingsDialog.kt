package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsDialog(
    isSoundEnabled: Boolean,
    isHapticsEnabled: Boolean,
    sfxVolume: Float,
    onSaveSettings: (Boolean, Boolean, Float) -> Unit,
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit
) {
    var soundState by remember { mutableStateOf(isSoundEnabled) }
    var hapticsState by remember { mutableStateOf(isHapticsEnabled) }
    var volumeState by remember { mutableFloatStateOf(sfxVolume) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Game Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings",
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sound SFX Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Sound Effects",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Swooshes, coin chimes & buzzer",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    Switch(
                        checked = soundState,
                        onCheckedChange = {
                            soundState = it
                            onSaveSettings(soundState, hapticsState, volumeState)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0284C7)),
                        modifier = Modifier.testTag("sound_toggle")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Volume Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Volume (${(volumeState * 100).toInt()}%)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Slider(
                        value = volumeState,
                        onValueChange = {
                            volumeState = it
                            onSaveSettings(soundState, hapticsState, volumeState)
                        },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF0284C7),
                            activeTrackColor = Color(0xFF0284C7)
                        ),
                        modifier = Modifier.testTag("volume_slider")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Vibration & Haptics",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Tactile tap and error collision feedback",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    Switch(
                        checked = hapticsState,
                        onCheckedChange = {
                            hapticsState = it
                            onSaveSettings(soundState, hapticsState, volumeState)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0284C7)),
                        modifier = Modifier.testTag("haptics_toggle")
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Developer Attribution Info Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Developed By Saiyed Afak Ahmed",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tap Away Arrows v1.0 • 20 Handcrafted Solvable Levels",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reset Progress
                if (!showResetConfirm) {
                    Button(
                        onClick = { showResetConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Reset All Game Progress",
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onResetProgress()
                                showResetConfirm = false
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirm Reset", color = Color.White, fontSize = 13.sp)
                        }
                        Button(
                            onClick = { showResetConfirm = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color(0xFF475569), fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
