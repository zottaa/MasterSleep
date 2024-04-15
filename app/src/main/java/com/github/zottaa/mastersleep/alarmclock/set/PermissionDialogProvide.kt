package com.github.zottaa.mastersleep.alarmclock.set

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import com.github.zottaa.mastersleep.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            val builder = defaultDialog()
                .setMessage(dialogDenialProvide(permission))
            builder.setPositiveButton(context.getString(R.string.settings)) { dialog: DialogInterface, _: Int ->
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
            val builder = defaultDialog()
                .setMessage(dialogRationaleProvide(permission))
                .setPositiveButton(context.getString(R.string.ok)) { dialog: DialogInterface, _: Int ->
                    activityResultLauncher.launch(arrayOf(permission))
                    dialog.dismiss()

                }
            builder.show()
        }

        private fun defaultDialog() =
            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.permission_required))
                .setNegativeButton(context.getString(R.string.cancel_button)) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()

                }


        private fun dialogRationaleProvide(permission: String): String =
            when (permission) {
                POST_NOTIFICATIONS -> context.getString(R.string.rationale_dialog_post_notification)
                ACTIVITY_RECOGNITION ->
                    context.getString(R.string.rationale_dialog_activity_recognition)

                else -> context.getString(R.string.unknown_permission, permission)
            }

        private fun dialogDenialProvide(permission: String): String =
            when (permission) {
                POST_NOTIFICATIONS -> context.getString(R.string.denial_dialog_post_notification)
                ACTIVITY_RECOGNITION ->
                    context.getString(R.string.denial_dialog_activity_recognition)

                else -> context.getString(R.string.unknown_permission, permission)
            }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION
    }
}