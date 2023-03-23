package com.tool.pomodoro.technique.tool.strategy.service.todotodaymove;

import com.tool.pomodoro.technique.tool.factory.FileStorageStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryAddDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TodoTodayMoveStrategyTests {

    private static TodoTodayMoveStrategy todoTodayMoveStrategy;
    private static TodoStrategy todoStrategy;
    private static TodayStrategy todayStrategy;
    private static TodoCategoryStrategy todoCategoryStrategy;

    @BeforeAll
    static void init() {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(new TestFilePathConfig());
        todoTodayMoveStrategy = strategyFactory.createTodoTodayMoveStrategy();
        todoStrategy = strategyFactory.createTodoStrategy();
        todayStrategy = strategyFactory.createTodayStrategy();
        todoCategoryStrategy = strategyFactory.createTodoCategoryStrategy();
    }

    @Test
    void copyTodoToToday() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("测试复制Todo到Today"));

        var todoAddDto = new TodoAddDto("测试复制Todo到Today", categoryIdOpt.get());
        String uuid = todoStrategy.add(todoAddDto);

        Optional<String> todayIdOpt = todoTodayMoveStrategy.copyTodoToToday(uuid);

        Assertions.assertTrue(todoStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todayIdOpt.isPresent());
        Assertions.assertTrue(todayStrategy.get(todayIdOpt.get()).isPresent());
    }


    @Test
    void cutTodoToToday() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("测试剪切Todo到Today"));
        var todoAddDto = new TodoAddDto("测试剪切Todo到Today", categoryIdOpt.get());
        String uuid = todoStrategy.add(todoAddDto);

        Optional<String> todayIdOpt = todoTodayMoveStrategy.cutTodoToToday(uuid);

        Assertions.assertFalse(todoStrategy.get(uuid).isPresent());
        Assertions.assertTrue(todayIdOpt.isPresent());
        Assertions.assertTrue(todayStrategy.get(todayIdOpt.get()).isPresent());
    }

}
