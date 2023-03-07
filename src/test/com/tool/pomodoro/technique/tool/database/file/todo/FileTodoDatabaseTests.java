package com.tool.pomodoro.technique.tool.database.file.todo;

import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.database.file.todo.FileTodoDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.todo.po.Todo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileTodoDatabaseTests {

    private final FileTodoDatabase todoDatabase = new FileTodoDatabase();

    @BeforeEach
    void initAll() {
        todoDatabase.init();
    }

    @Test
    void init() {
        Optional<File> todoFile = FileUtil.getTodoFile()
                .filter(file -> file.length() > 0);
        Optional<List<Todo>> todos = todoDatabase.selectAll();
        Assertions.assertEquals(todoFile.isPresent(), todos.isPresent());
    }

    @Test
    void save() {
        var todo = new Todo();
        todo.setId(UUID.randomUUID().toString());
        todo.setContent("test save");
        todo.setCreateTime(LocalDateTime.now());
        todoDatabase.save(todo);

        todoDatabase.init();
        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var todo = new Todo();
        todo.setId(UUID.randomUUID().toString());
        todo.setContent("test saveBatch1");
        todo.setCreateTime(LocalDateTime.now());

        var todo1 = new Todo();
        todo1.setId(UUID.randomUUID().toString());
        todo1.setContent("test saveBatch2");
        todo1.setCreateTime(LocalDateTime.now());

        todoDatabase.saveBatch(List.of(todo, todo1));

        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void selectAll() {
        var todo = new Todo();
        todo.setId(UUID.randomUUID().toString());
        todo.setContent("test selectAll");
        todo.setCreateTime(LocalDateTime.now());
        todoDatabase.save(todo);

        Optional<List<Todo>> todos = todoDatabase.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void selectById() {

        String uuid = UUID.randomUUID().toString();
        var todo = new Todo();
        todo.setId(uuid);
        todo.setContent("test selectById");
        todo.setCreateTime(LocalDateTime.now());
        todoDatabase.save(todo);

        Optional<Todo> todos = todoDatabase.selectById(uuid);

        Assertions.assertTrue(todos.isPresent());
    }
}
