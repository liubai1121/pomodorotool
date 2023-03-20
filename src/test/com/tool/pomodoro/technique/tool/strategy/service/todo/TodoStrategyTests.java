package com.tool.pomodoro.technique.tool.strategy.service.todo;

import com.tool.pomodoro.technique.tool.factory.FileStorageStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class TodoStrategyTests {
    private static TodoStrategy strategy;
    private static TodoCategoryStrategy todoCategoryStrategy;

    @BeforeAll
    static void init() {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(new TestFilePathConfig());
        strategy = strategyFactory.createTodoStrategy();
        todoCategoryStrategy = strategyFactory.createTodoCategoryStrategy();
    }

    @Test
    void add() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("添加的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("周报", categoryId);
        String id = strategy.add(todoAddDto);

        Assertions.assertNotNull(id);

        assertCategory(id, categoryId);
    }

    @Test
    void delete() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("删除的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        var dto = new TodoAddDto("测试删除方法", categoryId);
        String id = strategy.add(dto);
        strategy.delete(id);

        Assertions.assertTrue(strategy.get(id).isEmpty());
        Assertions.assertTrue(strategy.getCategory(id).isEmpty());
    }

    @Test
    void update() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("更新的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        var dto = new TodoAddDto("测试更新方法", categoryId);
        String id = strategy.add(dto);

        var updateContent = "测试更新成功";
        var updateDto = new TodoUpdateDto(id, updateContent);
        strategy.update(updateDto);

        Optional<TodoDto> today = strategy.get(id);

        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(today.get().content(), updateContent);
    }

    @Test
    void moveCategory() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("添加的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("测试移动", categoryId);
        String id = strategy.add(todoAddDto);

        assertCategory(id, categoryId);

        Optional<String> moveCategoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("测试移动的分组"));
        String moveCategoryId = moveCategoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));
        strategy.moveCategory(id, moveCategoryId);

        assertCategory(id, moveCategoryId);
    }

    @Test
    void get() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("查询的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("安全考试", categoryId);
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);
        Optional<TodoDto> todo = strategy.get(id);
        Assertions.assertTrue(todo.isPresent());
    }

    @Test
    void getCategory() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("查找分类的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("周报", categoryId);
        String id = strategy.add(todoAddDto);

        assertCategory(id, categoryId);
    }

    @Test
    void all() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("查询全部的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("开发工作", categoryId);
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);
        Optional<List<TodoDto>> todos = strategy.all();
        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void listByCategory() {
        Optional<String> categoryIdOpt = todoCategoryStrategy.add(new TodoCategoryAddDto("查询分类的分组"));
        String categoryId = categoryIdOpt.orElseThrow(() -> new RuntimeException("Exception"));

        TodoAddDto todoAddDto = new TodoAddDto("测试查询分类", categoryId);
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);

        Optional<List<TodoDto>> todos = strategy.listByCategory(categoryId);
        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    private void assertCategory(String todoId, String categoryId) {
        Optional<TodoCategoryDto> categoryOpt = strategy.getCategory(todoId);

        Assertions.assertTrue(categoryOpt.isPresent());
        Assertions.assertEquals(categoryOpt.get().id(), categoryId);
    }
}
