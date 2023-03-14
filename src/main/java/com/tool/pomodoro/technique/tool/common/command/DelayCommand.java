package com.tool.pomodoro.technique.tool.common.command;

import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class DelayCommand implements Command {

    private final LocalDateTime endTime;
    private final Command command;

    public DelayCommand(long time, ChronoUnit unit, Command command) {
        ChronoUnit chronoUnit = Optional.ofNullable(unit).orElse(ChronoUnit.MILLIS);
        endTime = LocalDateTime.now().plus(time, chronoUnit);
        this.command = command;
    }

    @Override
    public void execute() {
        if (LocalDateTime.now().isBefore(endTime)) {
            PerSecondCommandScheduleQueue.getInstance().put(this);
            return;
        }

        Optional.ofNullable(command)
                .ifPresent(Command::execute);
    }
}
