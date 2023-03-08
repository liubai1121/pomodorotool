package com.tool.pomodoro.technique.tool.controller.countdown.command;

import com.tool.pomodoro.technique.tool.ToolApplication;
import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.command.DelayCommand;
import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RemindCommand implements Command {
    @Override
    public void execute() {
        Platform.runLater(this::createRemindWindow);
    }

    private void createRemindWindow() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource("remind.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);

        stage.show();
        var closeWindowCommand = new CloseWindowCommand(stage);
        DelayCommand delayCommand = new DelayCommand(30, ChronoUnit.SECONDS, List.of(closeWindowCommand));
        PerSecondCommandQueue.join(delayCommand);
    }
}
