package com.tool.pomodoro.technique.tool.controller.controller.today.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

public class TodayCountdownCommand implements Command {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Label label;
    private final Command command;

    public TodayCountdownCommand(Label label, Command command) {
        this.label = label;
        this.command = command;
    }

    private boolean isCancel = false;

    @Override
    public void execute() {
        if (isCancel) {
            return;
        }
        Optional.of(label.getText())
                .filter(Predicate.not(String::isBlank))
                .map(this::countdown)
                .ifPresent(newTime -> PerSecondCommandScheduleQueue.getInstance().put(this));
    }

    public void cancel() {
        isCancel = true;
    }

    private LocalTime countdown(String text) {
        LocalTime localTime = LocalTime.parse(text, dateTimeFormatter);
        LocalTime newTime = localTime.plusSeconds(-1);

        Platform.runLater(() -> label.setText(dateTimeFormatter.format(newTime)));

        boolean isCountdownEnds = newTime.toSecondOfDay() == 0;
        if (isCountdownEnds) {
            Optional.ofNullable(command)
                    .ifPresent(Command::execute);
            return null;
        }
        return newTime;
    }
}
