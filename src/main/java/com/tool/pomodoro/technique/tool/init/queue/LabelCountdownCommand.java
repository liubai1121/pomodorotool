package com.tool.pomodoro.technique.tool.init.queue;


import javafx.scene.control.Label;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LabelCountdownCommand implements Command {

    private final Label label;
    private final String id;
    private final Consumer<String> consumer;
    private final CommandQueue commandQueue;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LabelCountdownCommand(Label label, String id, Consumer<String> consumer, CommandQueue commandQueue) {
        this.label = label;
        this.id = id;
        this.consumer = consumer;
        this.commandQueue = commandQueue;
    }

    @Override
    public void execute() {
        Optional.of(label.getText())
                .filter(Predicate.not(String::isBlank))
                .map(this::countdown)
                .ifPresent(newTime -> commandQueue.join(this));
    }

    private LocalTime countdown(String text) {
        LocalTime localTime = LocalTime.parse(text, dateTimeFormatter);
        LocalTime newTime = localTime.plusSeconds(-1);
        label.setText(dateTimeFormatter.format(newTime));

        boolean isCountdownEnds = newTime.toSecondOfDay() == 0;
        if (isCountdownEnds) {
            consumer.accept(id);
            return null;
        }
        return newTime;
    }
}
