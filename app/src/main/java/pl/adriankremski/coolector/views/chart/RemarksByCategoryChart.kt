package pl.adriankremski.coolector.views.chart

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import pl.adriankremski.coolector.model.StatisticEntry
import pl.adriankremski.coolector.views.chart.RemarksChartValueFormatter
import pl.adriankremski.coolector.statistics.StatisticsActivity
import pl.adriankremski.coolector.utils.uppercaseFirstLetter

class RemarksByCategoryChart : BarChart {
    constructor(context: Context?) : super(context) {
        initChart()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initChart()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initChart()
    }

    fun initChart() {
        setDrawBarShadow(false);
        setDrawValueAboveBar(true);
        description.isEnabled = false;
        setMaxVisibleValueCount(60);
        setPinchZoom(false);
        setDrawGridBackground(false);

        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setDrawGridLines(false);

        axisLeft.setDrawAxisLine(false)
        axisLeft.setDrawGridLines(false)
        axisLeft.setDrawLabels(false)

        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false)
        axisRight.setDrawAxisLine(false)

        xAxis.setDrawGridLines(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)

        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        legend.orientation = Legend.LegendOrientation.HORIZONTAL;
        legend.setDrawInside(false);
        legend.form = Legend.LegendForm.SQUARE;
        legend.formSize = 9f;
        legend.textSize = 11f;
        legend.xEntrySpace = 4f;

    }

    fun setStatistics(categoryStatistics: List<StatisticEntry>) {
        var dataSets = mutableListOf<IBarDataSet>()

        for ((valuePosition, catStatistics) in categoryStatistics.withIndex()) {
            var values = mutableListOf<BarEntry>()
            values.add(BarEntry(valuePosition.toFloat(), catStatistics.reportedCount.toFloat()))
            var set = BarDataSet(values, catStatistics.name.uppercaseFirstLetter())
            set.color = getCategoryColor(catStatistics.name)
            dataSets.add(set);
        }

        var data = BarData(dataSets);
        data.setValueTextSize(10f);
        data.barWidth = 0.9f;
        data.setValueFormatter(RemarksChartValueFormatter())

        this.data = data;
        animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }

    fun  getCategoryColor(name: String): Int {
        return when(name.toLowerCase()) {
            "litter" -> Color.parseColor("#E65100")
            "damages" -> Color.parseColor("#E53935")
            "accidents" -> Color.parseColor("#757575")
            else -> Color.parseColor("#0288D1")
        }
    }
}
