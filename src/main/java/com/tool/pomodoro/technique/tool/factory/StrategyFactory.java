package com.tool.pomodoro.technique.tool.factory;

import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;

public interface StrategyFactory {

    TodoStrategy createTodoStrategy();

    TodoCategoryStrategy createTodoCategoryStrategy();

    TodayStrategy createTodayStrategy();

    TodayReportStrategy createTodayReportStrategy();

    LabelStrategy createLabelStrategy();

    TodoTodayMoveStrategy createTodoTodayMoveStrategy();
}
