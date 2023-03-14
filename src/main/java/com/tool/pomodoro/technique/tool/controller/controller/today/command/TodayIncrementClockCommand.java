package com.tool.pomodoro.technique.tool.controller.controller.today.command;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.factory.today.TodayStrategyFactory;

public class TodayIncrementClockCommand implements Command {

    private final String id;

    public TodayIncrementClockCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute() {
        TodayStrategy todayStrategy = TodayStrategyFactory.create();
        todayStrategy.incrementClock(id);
    }
}
