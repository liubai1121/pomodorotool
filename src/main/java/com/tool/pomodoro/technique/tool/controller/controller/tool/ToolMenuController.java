package com.tool.pomodoro.technique.tool.controller.controller.tool;

import com.tool.pomodoro.technique.tool.controller.controller.label.LabelManagementController;
import com.tool.pomodoro.technique.tool.controller.controller.report.ReportController;
import com.tool.pomodoro.technique.tool.controller.controller.report.TodayLineChartReportController;
import com.tool.pomodoro.technique.tool.controller.controller.report.TodayTableReportController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import javafx.fxml.FXML;
import javafx.util.Callback;

public class ToolMenuController {

    private final StrategyFactory strategyFactory;

    public ToolMenuController(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @FXML
    protected void onLabelManagement() {
        var labelManagement = new LabelManagementController(strategyFactory.createLabelStrategy());
        var stage = WindowUtil.create("标签管理", "label/label-management.fxml", labelManagement);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    @FXML
    protected void onReportView() {
        var reportController = new ReportController(strategyFactory);
        var reportTableController = new TodayTableReportController(reportController, strategyFactory.createTodayReportStrategy());
        var reportLineChartController = new TodayLineChartReportController(reportController, strategyFactory.createTodayReportStrategy());

        Callback<Class<?>, Object> controllerFactory = type -> {
            if (type == ReportController.class) {
                return reportController;
            } else if (type == TodayTableReportController.class) {
                return reportTableController;
            } else if (type == TodayLineChartReportController.class) {
                return reportLineChartController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc);
                }
            }
        };

        var stage = WindowUtil.create("报表", "report/report-view.fxml", controllerFactory);
        stage.show();
    }
}
