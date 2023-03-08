package com.tool.pomodoro.technique.tool.common.command;

import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DelayCommand implements Command {

    private final LocalDateTime endTime;
    private final List<Command> commands;

    public DelayCommand(long time, ChronoUnit unit, List<Command> commands) {
        ChronoUnit chronoUnit = Optional.ofNullable(unit).orElse(ChronoUnit.MILLIS);
        endTime = LocalDateTime.now().plus(time, chronoUnit);
        this.commands = commands;
    }

    @Override
    public void execute() {
        if (LocalDateTime.now().isBefore(endTime)) {
            PerSecondCommandQueue.join(this);
            return;
        }

        Optional.ofNullable(commands)
                .map(commandList -> commandList.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(commandList -> commandList.forEach(Command::execute));
    }
}
