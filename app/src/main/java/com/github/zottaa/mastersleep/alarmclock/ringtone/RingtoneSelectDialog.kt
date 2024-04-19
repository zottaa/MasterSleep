package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.app.Dialog
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.github.zottaa.mastersleep.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RingtoneSelectDialog : DialogFragment() {
    private lateinit var currentUri: Uri
    private val viewModel: RingtoneSelectViewModel by activityViewModels()
    private lateinit var ringtones: List<Ringtone>
    private var playingRingtone: android.media.Ringtone? = null

    private val adapter by lazy {
        SimpleAdapter(
            requireContext(),
            ringtones.map { mapOf("title" to it.title) },
            android.R.layout.simple_list_item_single_choice,
            arrayOf("title"),
            intArrayOf(android.R.id.text1)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        ringtones = getAndroidRingtones()
        currentUri = viewModel.getRingtoneUri()
        val currentPosition = ringtones.indexOfFirst { currentUri == it.uri }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.select_ringtone))
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.setRingtoneUri(currentUri)
                dismiss()
            }
            .setNegativeButton(R.string.cancel_button) { _, _ ->
                dismiss()
            }
            .setSingleChoiceItems(adapter, currentPosition) { _, which ->
                playingRingtone?.stop()
                ringtones[which].also {
                    currentUri = it.uri
                    playingRingtone = RingtoneManager.getRingtone(requireContext(), currentUri)
                    playingRingtone?.play()
                }
            }
            .create()
    }

    override fun onPause() {
        super.onPause()
        playingRingtone?.stop()
    }

    private fun getAndroidRingtones(): List<Ringtone> {
        val ringtoneManager = RingtoneManager(requireContext())
        val cursor = ringtoneManager.cursor
        return (0 until cursor.count).map {
            cursor.moveToPosition(it)
            Ringtone(
                title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                uri = ringtoneManager.getRingtoneUri(it)
            )
        }
    }

    private data class Ringtone(
        val title: String,
        val uri: Uri
    )
}