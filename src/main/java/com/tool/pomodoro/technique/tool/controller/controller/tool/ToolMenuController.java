package com.tool.pomodoro.technique.tool.controller.controller.tool;

import com.tool.pomodoro.technique.tool.controller.controller.label.LabelManagementController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import javafx.fxml.FXML;

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
}
