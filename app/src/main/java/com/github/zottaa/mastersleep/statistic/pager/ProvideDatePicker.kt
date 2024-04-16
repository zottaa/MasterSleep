package com.github.zottaa.mastersleep.statistic.pager

import android.content.Context
import androidx.core.util.Pair
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ProvideDatePicker {
    fun provide(currentDateRange: Pair<String, String>): MaterialDatePicker<Pair<Long, Long>>

    class Base @Inject constructor(
        @ApplicationContext
        private val context: Context,
        private val dateUtils: DateUtils
    ) : ProvideDatePicker {
        override fun provide(currentDateRange: Pair<String, String>): MaterialDatePicker<Pair<Long, Long>> =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(context.getString(R.string.select_dates))
                .setSelection(
                    Pair(
                        dateUtils.stringDateToLong(currentDateRange.first),
                        dateUtils.stringDateToLong(currentDateRange.second)
                    )
                )
                .build()

    }
}