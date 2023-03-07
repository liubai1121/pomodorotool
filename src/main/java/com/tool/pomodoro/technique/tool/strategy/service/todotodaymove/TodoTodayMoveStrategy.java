package com.tool.pomodoro.technique.tool.strategy.service.todotodaymove;

import java.util.Optional;

public interface TodoTodayMoveStrategy {
    Optional<String> copyTodoToToday(String uuid);

    Optional<String> cutTodoToToday(String uuid);

    Optional<String> copyTodayToTodo(String uuid);

    Optional<String> cutTodayToTodo(String uuid);
}
