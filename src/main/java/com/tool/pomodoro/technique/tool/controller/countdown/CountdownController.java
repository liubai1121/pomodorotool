package com.tool.pomodoro.technique.tool.controller.countdown;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.controller.countdown.command.LabelCountdownCommand;
import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class CountdownController {

    public CountdownController() {
    }

    @FXML
    private Label countdownLabel;

    public void startCountdown(List<Command> commands) {
        var labelCountdownCommand = new LabelCountdownCommand(countdownLabel, commands);
        PerSecondCommandQueue.join(labelCountdownCommand);
    }

}
