package com.tool.pomodoro.technique.tool.controller.countdown.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class CloseWindowCommand implements Command {

    private final Stage stage;

    public CloseWindowCommand(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> {
            Optional.ofNullable(stage.getOnCloseRequest())
                    .ifPresent(onCloseRequest -> {
                        WindowEvent event = new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST);
                        onCloseRequest.handle(event);
                    });

            stage.close();
        });
    }
}
