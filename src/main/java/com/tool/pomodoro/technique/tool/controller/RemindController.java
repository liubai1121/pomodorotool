package com.tool.pomodoro.technique.tool.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Optional;

public class RemindController {

    @FXML
    public Button closeRemindButton;

    @FXML
    protected void onCloseRemindButton() {
        Optional.ofNullable(closeRemindButton.getScene())
                .map(Scene::getWindow)
                .ifPresent(window -> ((Stage)window).close());
    }
}
