package com.tool.pomodoro.technique.tool.controller.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandQueue;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

public class LabelCountdownCommand implements Command {

    private final Label label;
    private final Command command;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LabelCountdownCommand(Label label, Command command) {
        this.label = label;
        this.command = command;
    }

    @Override
    public void execute() {
        Optional.of(label.getText())
                .filter(Predicate.not(String::isBlank))
                .map(this::countdown)
                .ifPresent(newTime -> PerSecondCommandQueue.getInstance().put(this));
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
