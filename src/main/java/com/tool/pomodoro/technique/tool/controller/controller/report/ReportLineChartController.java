package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportLineChartController implements Initializable {

    private final ReportController reportController;

    public ReportLineChartController(ReportController reportController) {
        this.reportController = reportController;
    }

    @FXML
    private LineChart<String, Number> reportLineChart;


    @FXML
    protected void onReportLineChartQuery() {
        Optional<TodayStatisticsDto> statisticsOpt = reportController.getStatistics();

        statisticsOpt.ifPresent(statistics -> {
            Map<LocalDate, Integer> clocksPerDay = statistics.clocksPerDay();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            clocksPerDay.forEach((day, clocks) -> {
                series.getData().add(new XYChart.Data<>(DateTimeFormatter.ISO_LOCAL_DATE.format(day), clocks));
            });
            reportLineChart.setData(FXCollections.observableArrayList(series));
        });

        if (statisticsOpt.isEmpty()) {
            reportLineChart.getData().clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportLineChart.setTitle("时钟数");
    }
}
