package com.tool.pomodoro.technique.tool.controller.countdown.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LabelCountdownCommand implements Command {

    private final Label label;
    private final List<Command> commands;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LabelCountdownCommand(Label label, List<Command> commands) {
        this.label = label;
        this.commands = commands;
    }

    @Override
    public void execute() {
        Optional.of(label.getText())
                .filter(Predicate.not(String::isBlank))
                .map(this::countdown)
                .ifPresent(newTime -> PerSecondCommandQueue.join(this));
    }

    private LocalTime countdown(String text) {
        LocalTime localTime = LocalTime.parse(text, dateTimeFormatter);
        LocalTime newTime = localTime.plusSeconds(-1);

        Platform.runLater(() -> label.setText(dateTimeFormatter.format(newTime)));

        boolean isCountdownEnds = newTime.toSecondOfDay() == 0;
        if (isCountdownEnds) {
            Platform.runLater(() -> label.setText("休息！！！"));

            Optional.ofNullable(commands)
                    .filter(Predicate.not(Collection::isEmpty))
                    .ifPresent(commands -> commands.forEach(Command::execute));
            return null;
        }
        return newTime;
    }
}
