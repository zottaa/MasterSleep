package com.github.zottaa.mastersleep.diary.core

import android.content.Context
import android.content.DialogInterface
import com.github.zottaa.mastersleep.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface DeleteDialogProvide {
    fun showDeleteDialog(block: () -> Unit)

    class Base(
        private val context: Context
    ) : DeleteDialogProvide {
        override fun showDeleteDialog(block: () -> Unit) {
            val builder = MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.delete_note))
                .setNegativeButton(
                    context.getString(
                        R.string.cancel_button
                    )
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .setMessage(context.getString(R.string.delete_note_dialog_text))
                .setPositiveButton(
                    context.getString(R.string.delete)
                ) { dialog: DialogInterface, _: Int ->
                    block()
                    dialog.dismiss()
                }
            builder.show()
        }
    }
}