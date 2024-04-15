package com.github.zottaa.mastersleep.settings

import android.content.Context
import com.github.zottaa.mastersleep.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface SettingsDialogProvide {
    fun provide(
        items: Array<String>,
        checkedItemIndex: Int,
        positiveButtonOnClick: () -> Unit,
        setSingleChoiceItemCallback: (Int) -> Unit
    ): MaterialAlertDialogBuilder

    abstract class Abstract(
        private val context: Context
    ) : SettingsDialogProvide {
        override fun provide(
            items: Array<String>,
            checkedItemIndex: Int,
            positiveButtonOnClick: () -> Unit,
            setSingleChoiceItemCallback: (Int) -> Unit
        ) = MaterialAlertDialogBuilder(context)
            .setNeutralButton(context.getString(R.string.cancel_button)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                positiveButtonOnClick.invoke()
                dialog.dismiss()
            }
            .setSingleChoiceItems(items, checkedItemIndex) { dialog, which ->
                setSingleChoiceItemCallback.invoke(which)
            }
    }

    class Language(
        private val context: Context,
    ) : Abstract(context) {
        override fun provide(
            items: Array<String>,
            checkedItemIndex: Int,
            positiveButtonOnClick: () -> Unit,
            setSingleChoiceItemCallback: (Int) -> Unit
        ) = super.provide(
            items,
            checkedItemIndex,
            positiveButtonOnClick,
            setSingleChoiceItemCallback
        )
            .setTitle(context.getString(R.string.language))
    }

    class Theme(
        private val context: Context
    ) : Abstract(context) {
        override fun provide(
            items: Array<String>,
            checkedItemIndex: Int,
            positiveButtonOnClick: () -> Unit,
            setSingleChoiceItemCallback: (Int) -> Unit
        ) = super.provide(
            items,
            checkedItemIndex,
            positiveButtonOnClick,
            setSingleChoiceItemCallback
        )
            .setTitle(context.getString(R.string.theme))
    }
}

