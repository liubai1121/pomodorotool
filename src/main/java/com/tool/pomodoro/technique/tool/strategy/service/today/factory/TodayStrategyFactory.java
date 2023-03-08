package com.tool.pomodoro.technique.tool.strategy.service.today.factory;

import com.tool.pomodoro.technique.tool.database.file.today.FileTodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.iml.TodayStrategyImpl;

public class TodayStrategyFactory {

    private final static FileTodayDatabase todayDatabase = new FileTodayDatabase();
    private final static TodayStrategy todayStrategy = new TodayStrategyImpl(todayDatabase);

    public static TodayStrategy create() {
        return todayStrategy;
    }
}
