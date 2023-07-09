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

public class TodoCategoryStrategyTests {
    private static TodoCategoryStrategy todoCategoryStrategy;
    private static TodoStrategy todoStrategy;

    @BeforeAll
    static void init() {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(new TestFilePathConfig());
        todoCategoryStrategy = strategyFactory.createTodoCategoryStrategy();
        todoStrategy = strategyFactory.createTodoStrategy();
    }

    @Test
    void add() {
        var addDto = new TodoCategoryAddDto("测试添加");
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);

        Assertions.assertTrue(idOpt.isPresent());
    }


    @Test
    void addDuplicate() {
        var addDto = new TodoCategoryAddDto("测试添加重复");
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);

        Assertions.assertTrue(idOpt.isPresent());

        var addDto1 = new TodoCategoryAddDto("测试添加重复");
        Optional<String> idOpt1 = todoCategoryStrategy.add(addDto1);
        Assertions.assertTrue(idOpt1.isPresent());

        Assertions.assertEquals(idOpt.get(), idOpt1.get());
    }

    @Test
    void delete() {
        var addDto = new TodoCategoryAddDto("测试删除");
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);

        Assertions.assertTrue(idOpt.isPresent());
        assertHas(idOpt.get());

        todoCategoryStrategy.delete(idOpt.get());

        Optional<TodoCategoryDto> todoCategoryOpt = todoCategoryStrategy.get(idOpt.get());
        Assertions.assertTrue(todoCategoryOpt.isEmpty());
    }

    @Test
    void deleteForTodos() {
        var addDto = new TodoCategoryAddDto("测试删除");
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);

        Assertions.assertTrue(idOpt.isPresent());
        assertHas(idOpt.get());

        todoStrategy.add(new TodoAddDto("测试删除分类1", idOpt.get()));
        todoStrategy.add(new TodoAddDto("测试删除分类2", idOpt.get()));
        todoStrategy.add(new TodoAddDto("测试删除分类3", idOpt.get()));

        Optional<List<TodoDto>> todoDtos = todoStrategy.listByCategory(idOpt.get());
        Assertions.assertTrue(todoDtos.isPresent());
        Assertions.assertEquals(todoDtos.get().size(), 3);

        todoCategoryStrategy.delete(idOpt.get());

        Optional<TodoCategoryDto> todoCategoryOpt = todoCategoryStrategy.get(idOpt.get());
        Assertions.assertTrue(todoCategoryOpt.isEmpty());

        todoDtos = todoStrategy.listByCategory(idOpt.get());
        Assertions.assertTrue(todoDtos.isEmpty());
    }

    @Test
    void update() {
        var name = "测试编辑";
        var addDto = new TodoCategoryAddDto(name);
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);
        Assertions.assertTrue(idOpt.isPresent());

        Optional<TodoCategoryDto> todoCategoryOpt = todoCategoryStrategy.get(idOpt.get());

        Assertions.assertTrue(todoCategoryOpt.isPresent());
        Assertions.assertEquals(todoCategoryOpt.get().name(), name);

        var updateName = "测试成功";
        var updateDto = new TodoCategoryUpdateDto(idOpt.get(), updateName);
        todoCategoryStrategy.update(updateDto);

        Optional<TodoCategoryDto> updatedTodoCategoryOpt = todoCategoryStrategy.get(idOpt.get());

        Assertions.assertTrue(updatedTodoCategoryOpt.isPresent());
        Assertions.assertEquals(updatedTodoCategoryOpt.get().name(), updateName);
    }

    @Test
    void get() {
        var addDto = new TodoCategoryAddDto("测试查找");
        Optional<String> idOpt = todoCategoryStrategy.add(addDto);
        Assertions.assertTrue(idOpt.isPresent());

        assertHas(idOpt.get());
    }

    private void assertHas(String id) {
        Optional<TodoCategoryDto> todoCategoryOpt = todoCategoryStrategy.get(id);
        Assertions.assertTrue(todoCategoryOpt.isPresent());
    }

    @Test
    void all() {
        var addDto = new TodoCategoryAddDto("测试查找全部");
        todoCategoryStrategy.add(addDto);

        Optional<List<TodoCategoryDto>> todoCategoryOpt = todoCategoryStrategy.all();

        Assertions.assertTrue(todoCategoryOpt.isPresent());
        Assertions.assertFalse(todoCategoryOpt.get().isEmpty());
    }
}
