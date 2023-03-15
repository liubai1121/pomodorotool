package com.tool.pomodoro.technique.tool.controller.controller.today.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;

public class TodayIncrementClockCommand implements Command {

    private final String id;
    private final TodayStrategy todayStrategy;

    public TodayIncrementClockCommand(TodayStrategy todayStrategy, String id) {
        this.todayStrategy = todayStrategy;
        this.id = id;
    }

    @Override
    public void execute() {
        todayStrategy.incrementClock(id);
    }
}
