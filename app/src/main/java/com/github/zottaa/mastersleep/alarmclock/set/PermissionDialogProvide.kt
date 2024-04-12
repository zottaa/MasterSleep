package com.github.zottaa.mastersleep.alarmclock.set

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi

interface PermissionDialogProvide {
    fun showPermissionDenialDialog(permission: String)

    fun showPermissionRationaleDialog(
        activityResultLauncher: ActivityResultLauncher<Array<String>>,
        permission: String
    )

    class Base(
        private val activity: Activity,
        private val context: Context
    ) : PermissionDialogProvide {
        override fun showPermissionDenialDialog(permission: String) {
            val builder = AlertDialog.Builder(context).apply {
                setMessage(dialogMessage(dialogDenialProvide(permission)))
                setTitle("Permission required")
                setCancelable(false)
                setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
            builder.setPositiveButton("Settings") { dialog: DialogInterface, _: Int ->
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                        "package",
                        activity.packageName,
                        this::class.java.name
                    )
                ).also(context::startActivity)
                dialog.dismiss()
            }
            builder.show()
        }

        override fun showPermissionRationaleDialog(
            activityResultLauncher: ActivityResultLauncher<Array<String>>,
            permission: String
        ) {
            val builder = AlertDialog.Builder(context).apply {
                setTitle("Permission required")
                setMessage(dialogMessage(dialogRationaleProvide(permission)))
                setCancelable(false)
                setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                    activityResultLauncher.launch(arrayOf(permission))
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }
            builder.show()
        }

        private fun dialogRationaleProvide(permission: String): DialogMessageProvide =
            when (permission) {
                POST_NOTIFICATIONS -> RationaleDialogMessageProvide.PostNotification()
                ACTIVITY_RECOGNITION -> RationaleDialogMessageProvide.ActivityRecognition()
                else -> DialogMessageProvide.Empty
            }

        private fun dialogDenialProvide(permission: String): DialogMessageProvide =
            when (permission) {
                POST_NOTIFICATIONS -> DenialDialogMessageProvide.PostNotification()
                ACTIVITY_RECOGNITION -> DenialDialogMessageProvide.ActivityRecognition()
                else -> DialogMessageProvide.Empty
            }

        private fun dialogMessage(dialogMessageProvide: DialogMessageProvide) =
            dialogMessageProvide.message()

    }
    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION
    }
}