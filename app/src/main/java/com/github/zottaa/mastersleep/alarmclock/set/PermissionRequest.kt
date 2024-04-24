package com.github.zottaa.mastersleep.alarmclock.set

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionRequest(
    private val activity: FragmentActivity,
    private val context: Context,
    private val block: () -> Unit
) {
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    init {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allPermissionGranted = permissions.all { it.value }
            if (allPermissionGranted) {
                block()
            } else {
                permissions.entries.forEach { entry ->
                    val permission = entry.key
                    val isGranted = entry.value
                    val permissionDialogProvide by lazy {
                        PermissionDialogProvide.Base(
                            activity,
                            context
                        )
                    }
                    if (!isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    ) {
                        permissionDialogProvide.showPermissionDenialDialog(
                            permission
                        )
                        return@registerForActivityResult
                    }
                }
                requestRuntimePermission(requestPermissionLauncher)
            }
        }
    }

    fun requestRuntimePermission(
        activityResultLauncher: ActivityResultLauncher<Array<String>>,
    ) {
        val permissionsToRequest = buildPermissionsList()

        when {
            permissionsToRequest.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            } -> {
                block()
            }

            permissionsToRequest.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            } -> {
                val permissionDialogProvide by lazy {
                    PermissionDialogProvide.Base(
                        activity,
                        context
                    )
                }
                permissionsToRequest.forEach { permission ->
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    )
                        permissionDialogProvide.showPermissionRationaleDialog(
                            activityResultLauncher,
                            permission
                        )
                }
            }

            else -> activityResultLauncher.launch(permissionsToRequest)
        }
    }

    private fun buildPermissionsList(): Array<String> {
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                POST_NOTIFICATIONS,
                ACTIVITY_RECOGNITION
            )
        } else {
            arrayOf(ACTIVITY_RECOGNITION)
        }
        return permissionsToRequest
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION
    }
}