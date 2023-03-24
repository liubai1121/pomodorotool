package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class TodayCategoryPieChartReportController implements Initializable {

    private final ReportController reportController;
    private final TodayReportStrategy todayReportStrategy;

    public TodayCategoryPieChartReportController(ReportController reportController, TodayReportStrategy todayReportStrategy) {
        this.reportController = reportController;
        this.todayReportStrategy = todayReportStrategy;
    }

    @FXML
    private PieChart reportCategoryPieChart;


    @FXML
    protected void onQuery() {
        var pieChartForCategoryOpt =
                reportController.getSelectDuration()
                        .flatMap(pair -> todayReportStrategy.pieChartForCategory(pair.getValue0(), pair.getValue1()));

        pieChartForCategoryOpt.ifPresent(pieChartForCategory -> {
            var list = new ArrayList<PieChart.Data>();
            pieChartForCategory.dataMap().forEach((category, proportion) -> {
                list.add(new PieChart.Data(category, proportion));
            });
            reportCategoryPieChart.setData(FXCollections.observableArrayList(list));
        });

        if (pieChartForCategoryOpt.isEmpty()) {
            reportCategoryPieChart.getData().clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportCategoryPieChart.setTitle("分类用时");
    }
}
