package com.tool.pomodoro.technique.tool.strategy.service.todo.factory;

import com.tool.pomodoro.technique.tool.database.file.todo.FileTodoDatabase;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.impl.TodoStrategyImpl;

public class TodoStrategyFactory {

    private final static FileTodoDatabase todayDatabase = new FileTodoDatabase();
    private final static TodoStrategy todoStrategy = new TodoStrategyImpl(todayDatabase);

    public static TodoStrategy create() {
        return todoStrategy;
    }
}
