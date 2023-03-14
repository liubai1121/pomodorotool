package com.tool.pomodoro.technique.tool.strategy.service.todotodaymove;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.factory.today.TodayStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.factory.todo.TodoStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl.TodoTodayMoveStrategyImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TodoTodayMoveStrategyTests {

    private final TodayStrategy todayStrategy = TodayStrategyFactory.create();
    private final TodoStrategy todoStrategy = TodoStrategyFactory.create();

    private final TodoTodayMoveStrategy todoTodayMoveStrategy = new TodoTodayMoveStrategyImpl(todoStrategy, todayStrategy);

    @Test
    void copyTodoToToday() {
        var todoAddDto = new TodoAddDto("测试复制Todo到Today");
        String uuid = todoStrategy.add(todoAddDto);

        Optional<String> todayIdOpt = todoTodayMoveStrategy.copyTodoToToday(uuid);

        Assertions.assertTrue(todoStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todayIdOpt.isPresent());
        Assertions.assertTrue(todayStrategy.get(todayIdOpt.get()).isPresent());
    }


    @Test
    void cutTodoToToday() {
        var todoAddDto = new TodoAddDto("测试剪切Todo到Today");
        String uuid = todoStrategy.add(todoAddDto);

        Optional<String> todayIdOpt = todoTodayMoveStrategy.cutTodoToToday(uuid);

        Assertions.assertFalse(todoStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todayIdOpt.isPresent());
        Assertions.assertTrue(todayStrategy.get(todayIdOpt.get()).isPresent());
    }

    @Test
    void copyTodayToTodo() {
        var todayAddDto = new TodayAddDto("测试复制Today到Todo");
        String uuid = todayStrategy.add(todayAddDto);

        Optional<String> todoIdOpt = todoTodayMoveStrategy.copyTodayToTodo(uuid);

        Assertions.assertTrue(todayStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todoIdOpt.isPresent());
        Assertions.assertTrue(todoStrategy.get(todoIdOpt.get()).isPresent());
    }

    @Test
    void cutTodayToTodo() {
        var todayAddDto = new TodayAddDto("测试剪切Today到Todo");
        String uuid = todayStrategy.add(todayAddDto);

        Optional<String> todoIdOpt = todoTodayMoveStrategy.cutTodayToTodo(uuid);

        Assertions.assertFalse(todayStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todoIdOpt.isPresent());
        Assertions.assertTrue(todoStrategy.get(todoIdOpt.get()).isPresent());
    }

}
