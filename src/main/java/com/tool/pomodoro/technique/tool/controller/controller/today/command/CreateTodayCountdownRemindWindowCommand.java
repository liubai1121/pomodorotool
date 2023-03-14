package com.tool.pomodoro.technique.tool.controller.controller.today.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.command.DelayCommand;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.time.temporal.ChronoUnit;

public class CreateTodayCountdownRemindWindowCommand implements Command {
    @Override
    public void execute() {
        Platform.runLater(this::createRemindWindow);
    }

    private void createRemindWindow() {
        Stage stage = WindowUtil.create("注意休息！", "today/today-countdown-remind.fxml");
        stage.setAlwaysOnTop(true);
        stage.show();

        var closeWindowCommand = new CloseWindowCommand(stage);
        DelayCommand delayCommand = new DelayCommand(5, ChronoUnit.MINUTES, closeWindowCommand);
        PerSecondCommandScheduleQueue.getInstance().put(delayCommand);
    }
}
