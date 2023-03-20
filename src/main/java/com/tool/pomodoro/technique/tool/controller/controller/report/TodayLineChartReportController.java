package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class TodayLineChartReportController implements Initializable {

    private final ReportController reportController;
    private final TodayReportStrategy todayReportStrategy;

    public TodayLineChartReportController(ReportController reportController, TodayReportStrategy todayReportStrategy) {
        this.reportController = reportController;
        this.todayReportStrategy = todayReportStrategy;
    }

    @FXML
    private LineChart<String, Number> reportLineChart;


    @FXML
    protected void onReportLineChartQuery() {
        var lineChartValueOpt =
                reportController.getSelectDuration()
                        .flatMap(pair -> todayReportStrategy.lineChart(pair.getValue0(), pair.getValue1()));

        lineChartValueOpt.ifPresent(lineChartValue -> {

            String[] xAxis = lineChartValue.xAxis();
            Number[] yAxis = lineChartValue.yAxis();

            if (xAxis.length != yAxis.length) {
                return;
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            var seriesData = series.getData();
            for (int i = 0; i < xAxis.length; i++) {
                seriesData.add(new XYChart.Data<>(xAxis[i], yAxis[i]));
            }

            reportLineChart.setData(FXCollections.observableArrayList(series));
        });

        if (lineChartValueOpt.isEmpty()) {
            reportLineChart.getData().clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportLineChart.setTitle("时钟数");
    }
}
