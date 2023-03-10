package com.tool.pomodoro.technique.tool.controller;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ToolMenuController {


    @FXML
    protected void onLabelManagement() {
        Stage stage = WindowUtil.create("标签管理", "label/label-management.fxml");
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
