package com.tool.pomodoro.technique.tool.controller.controller.today.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TodayCountdownCommand implements Command {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Label countdownLabel;
    private final Command command;
    private LocalTime countdownTime;

    public TodayCountdownCommand(Label countdownLabel, LocalTime countdownTime, Command command) {
        this.countdownLabel = countdownLabel;
        this.command = command;
        this.countdownTime = countdownTime;

        initLabelText();
    }

    private void initLabelText() {
        Optional.ofNullable(countdownLabel)
                .ifPresent(label ->
                        Optional.ofNullable(countdownTime).ifPresent(time -> {
                            label.setText(dateTimeFormatter.format(time));
                        }));
    }

    private boolean isStart = true;

    public synchronized void stop() {
        isStart = false;
        PerSecondCommandScheduleQueue.getInstance().delete(this);
    }

    public synchronized void start() {
        isStart = true;
    }

    public synchronized void complete() {
        isStart = false;
        Optional.ofNullable(command)
                .ifPresent(Command::execute);
    }

    @Override
    public void execute() {
        if (!isStart) {
            return;
        }

        if (this.countdown()) {
            PerSecondCommandScheduleQueue.getInstance().put(this);
            Optional.ofNullable(countdownLabel)
                    .ifPresent(countdownLabel -> Platform.runLater(() -> countdownLabel.setText(dateTimeFormatter.format(countdownTime))));
        } else {
            Optional.ofNullable(command)
                    .ifPresent(Command::execute);
        }
    }

    private boolean countdown() {
        return Optional.ofNullable(countdownTime)
                .map(time -> {
                    countdownTime = time.plusSeconds(-1);
                    boolean countdownIsNotOver = countdownTime.toSecondOfDay() != 0;
                    return countdownIsNotOver;
                }).orElse(false);
    }
}
