package com.example.lucas.haushaltsmanager.ReportBuilder.Widgets;

import android.content.Context;
import android.view.View;

import com.example.lucas.haushaltsmanager.Entities.Expense.ExpenseObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChartWidget implements Widget {
    private PieChart chart;

    public PieChartWidget(Context context) {
        initChartWithDefaultValues(context);
    }

    @Override
    public View getWidgetView() {
        return chart;
    }

    @Override
    public void setData(List<ExpenseObject> expenses) {
        // TODO: Create data set from expenses and add to PieChart
        chart.invalidate();
    }

    private void initChartWithDefaultValues(Context context) {
        this.chart = new PieChart(context);

        this.chart.setData(new PieData(new PieDataSet(
                new ArrayList<PieEntry>() {{
                    add(new PieEntry(100f, "Eintrag"));
                }}, "Test"
        )));
    }
}