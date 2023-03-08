package com.tool.pomodoro.technique.tool.common.command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CompositeCommand implements Command {

    private final List<Command> commands;

    public CompositeCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        Optional.ofNullable(commands)
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(list -> list.forEach(Command::execute));
    }
}
