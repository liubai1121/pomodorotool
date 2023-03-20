package com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;

import java.util.Optional;
import java.util.function.Predicate;

public class TodoTodayMoveStrategyImpl implements TodoTodayMoveStrategy {

    private final TodoStrategy todoStrategy;
    private final TodayStrategy todayStrategy;

    public TodoTodayMoveStrategyImpl(TodoStrategy todoStrategy, TodayStrategy todayStrategy) {
        this.todoStrategy = todoStrategy;
        this.todayStrategy = todayStrategy;
    }

    @Override
    public Optional<String> copyTodoToToday(String uuid) {
        return todoStrategy.get(uuid)
                .flatMap(todo -> todoStrategy.getCategory(todo.id())
                        .map(category -> todayStrategy.add(new TodayAddDto(todo.content(), category.name()))));

    }

    @Override
    public Optional<String> cutTodoToToday(String uuid) {
        Optional<String> todayIdOpt = copyTodoToToday(uuid);
        if (todayIdOpt.isPresent()) {
            todoStrategy.delete(uuid);
        }
        return todayIdOpt;
    }
}
