package com.tool.pomodoro.technique.tool.strategy.service.todo;

import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;
import com.tool.pomodoro.technique.tool.factory.todo.TodoStrategyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class TodoStrategyTests {

    private final TodoStrategy strategy = TodoStrategyFactory.create();

    @Test
    void enterTodo() {
        TodoAddDto todoAddDto = new TodoAddDto("周报");
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);
    }

    @Test
    void delete() {
        var dto = new TodoAddDto("测试删除方法");
        String id = strategy.add(dto);
        strategy.delete(id);
        Assertions.assertFalse(strategy.get(id).isPresent());
    }

    @Test
    void updateTodo() {
        var dto = new TodoAddDto("测试更新方法");
        String id = strategy.add(dto);

        var updateContent = "测试更新成功";
        var updateDto = new TodoUpdateDto(id, updateContent);
        strategy.update(updateDto);

        Optional<TodoDto> today = strategy.get(id);

        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(today.get().content(), updateContent);
    }

    @Test
    void selectTodo() {
        TodoAddDto todoAddDto = new TodoAddDto("安全考试");
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);
        Optional<TodoDto> todo = strategy.get(id);
        Assertions.assertTrue(todo.isPresent());
    }

    @Test
    void listTodo() {
        TodoAddDto todoAddDto = new TodoAddDto("开发工作");
        String id = strategy.add(todoAddDto);
        Assertions.assertNotNull(id);
        Optional<List<TodoDto>> todos = strategy.all();
        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }
}
