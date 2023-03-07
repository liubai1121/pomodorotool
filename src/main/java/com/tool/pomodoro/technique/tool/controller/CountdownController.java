package com.tool.pomodoro.technique.tool.controller;

import com.tool.pomodoro.technique.tool.init.ToolInit;
import com.tool.pomodoro.technique.tool.init.queue.CommandQueue;
import com.tool.pomodoro.technique.tool.init.queue.LabelCountdownCommand;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CountdownController implements ToolInit {

    private final String id;
    private final TodayStrategy todayStrategy;
    private final CommandQueue commandQueue;

    public CountdownController(String id, TodayStrategy todayStrategy, CommandQueue commandQueue) {
        this.id = id;
        this.commandQueue = commandQueue;
        this.todayStrategy = todayStrategy;
    }

    @FXML
    private Label countdownLabel;

    @Override
    public void init() {
        var labelCountdownCommand = new LabelCountdownCommand(countdownLabel, id, todayStrategy::incrementClock, commandQueue);
        commandQueue.join(labelCountdownCommand);
    }

}
