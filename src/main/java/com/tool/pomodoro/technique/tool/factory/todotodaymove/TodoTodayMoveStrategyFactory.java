package com.tool.pomodoro.technique.tool.factory.todotodaymove;

import com.tool.pomodoro.technique.tool.factory.today.TodayStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.todo.TodoStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl.TodoTodayMoveStrategyImpl;

public class TodoTodayMoveStrategyFactory {

    private final static TodoStrategy todoStrategy = TodoStrategyFactory.create();
    private final static TodayStrategy todayStrategy = TodayStrategyFactory.create();
    private final static TodoTodayMoveStrategy todoTodayMoveStrategy = new TodoTodayMoveStrategyImpl(todoStrategy, todayStrategy);

    public static TodoTodayMoveStrategy create() {
        return todoTodayMoveStrategy;
    }
}
