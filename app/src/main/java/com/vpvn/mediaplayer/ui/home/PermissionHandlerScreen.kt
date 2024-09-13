package com.vpvn.mediaplayer.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.vpvn.mediaplayer.R


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionHandlerScreen(multiplePermission: MultiplePermissionsState) {

    val context = LocalContext.current

    val showRationalDialog = remember { mutableStateOf(false) }
    if (showRationalDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog.value = false
            },
            title = {
                Text(
                    text = stringResource(R.string.permission),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    if (multiplePermission.revokedPermissions.size == 2) {
                        stringResource(id = R.string.permission_grant_audio_video)
                    } else if (multiplePermission.revokedPermissions.first().permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        stringResource(id = R.string.permission_grant_video)
                    } else {
                        stringResource(id = R.string.permission_grant_audio)
                    },
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)

                    }) {
                    Text(stringResource(R.string.ok), style = TextStyle(color = Color.Black))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                    }) {
                    Text(stringResource(R.string.cancel), style = TextStyle(color = Color.Black))
                }
            },
        )
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.permissions), color = Color.White) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {
                    if (!multiplePermission.allPermissionsGranted) {
                        if (multiplePermission.shouldShowRationale) {
                            // Show a rationale if needed (optional)
                            showRationalDialog.value = true
                        } else {
                            // Request the permission
                            multiplePermission.launchMultiplePermissionRequest()

                        }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.we_have_video_and_audio_permission),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text(text = stringResource(id = R.string.permission_request))
                }
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                    text = if (multiplePermission.allPermissionsGranted) {
                        stringResource(id = R.string.permission_granted)
                    } else if (multiplePermission.shouldShowRationale) {
                        // If the user has denied the permission but the rationale can be shown,
                        // then gently explain why the app requires this permission
                        if (multiplePermission.revokedPermissions.size == 2) {
                            stringResource(id = R.string.permission_grant_audio_video)
                        } else if (multiplePermission.revokedPermissions.first().permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            stringResource(id = R.string.permission_grant_video)
                        } else {
                            stringResource(id = R.string.permission_grant_audio)
                        }
                    } else {
                        // If it's the first time the user lands on this feature, or the user
                        // doesn't want to be asked again for this permission, explain that the
                        // permission is required
                        stringResource(id = R.string.permission_grant_audio_video)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}