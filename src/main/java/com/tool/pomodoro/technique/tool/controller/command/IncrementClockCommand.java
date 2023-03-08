package com.tool.pomodoro.technique.tool.controller.command;

import com.tool.pomodoro.technique.tool.common.queue.command.Command;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.factory.TodayStrategyFactory;

public class IncrementClockCommand implements Command {

    private final String id;

    public IncrementClockCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute() {
        TodayStrategy todayStrategy = TodayStrategyFactory.create();
        todayStrategy.incrementClock(id);
    }
}
