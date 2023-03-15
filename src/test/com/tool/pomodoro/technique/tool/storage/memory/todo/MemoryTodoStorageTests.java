package com.tool.pomodoro.technique.tool.storage.memory.todo;

import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class MemoryTodoStorageTests {

    private final MemoryTodoStorage todoDatabase = new MemoryTodoStorage();

    @Test
    void save() {
        var todo = new Todo(IdUtil.generate(), "test save");
        todoDatabase.save(todo);

        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var todo = new Todo(IdUtil.generate(), "test saveBatch1");

        var todo1 = new Todo(IdUtil.generate(), "test saveBatch2");

        todoDatabase.saveBatch(List.of(todo, todo1));

        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void selectAll() {
        var todo = new Todo(IdUtil.generate(), "test selectAll");
        todoDatabase.save(todo);

        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void selectById() {
        String uuid = IdUtil.generate();
        var todo = new Todo(uuid, "test selectById");
        todoDatabase.save(todo);

        Optional<Todo> todos = todoDatabase.selectById(uuid);

        Assertions.assertTrue(todos.isPresent());
    }
}
