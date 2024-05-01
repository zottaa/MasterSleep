package com.github.zottaa.mastersleep.statistic.sleep

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

interface BarChartConfigure {
    fun configure(barChart: BarChart, descriptionLabel: String, dataSet: List<BarEntry>)

    abstract class Abstract : BarChartConfigure {
        private val valueFormatterX by lazy {
            ValueFormatters.BarChartX()
        }
        private val valueFormatterY by lazy {
            ValueFormatters.BarChartY()
        }

        override fun configure(
            barChart: BarChart,
            descriptionLabel: String,
            dataSet: List<BarEntry>
        ) {
            barChart.axisRight.isEnabled = false
            val xAxis = barChart.xAxis
            val yAxis = barChart.axisLeft
            xAxis.valueFormatter = valueFormatterX
            xAxis.granularity = 1f
            yAxis.valueFormatter = valueFormatterY
            yAxis.axisMinimum = 0f

            val description = Description()
            description.text = ""
            barChart.description = description

            val barDataSet = BarDataSet(dataSet, descriptionLabel)

            barDataSet.setDrawValues(false)
            val barData = BarData(barDataSet)
            barChart.data = barData
        }
    }

    class Day : Abstract() {
        override fun configure(
            barChart: BarChart,
            descriptionLabel: String,
            dataSet: List<BarEntry>
        ) {
            super.configure(barChart, descriptionLabel, dataSet)
            val xAxis = barChart.xAxis
            val yAxis = barChart.axisLeft
            xAxis.textColor = Color.BLACK
            yAxis.textColor = Color.BLACK
        }
    }

    class Night : Abstract() {
        override fun configure(
            barChart: BarChart,
            descriptionLabel: String,
            dataSet: List<BarEntry>
        ) {
            super.configure(barChart, descriptionLabel, dataSet)
            val xAxis = barChart.xAxis
            val yAxis = barChart.axisLeft
            xAxis.textColor = Color.WHITE
            yAxis.textColor = Color.WHITE
        }
    }
}