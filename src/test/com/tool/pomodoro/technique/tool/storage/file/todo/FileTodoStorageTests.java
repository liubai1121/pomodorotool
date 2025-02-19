package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class FileTodoStorageTests {

    private final FileTodoStorage todoDatabase =
            FileTodoStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

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


    @Test
    void selectByIds() {
        String uuid = IdUtil.generate();
        var todo = new Todo(uuid, "test selectByIds");
        todoDatabase.save(todo);

        String uuid1 = IdUtil.generate();
        var todo1 = new Todo(uuid1, "test selectByIds1");
        todoDatabase.save(todo1);

        Optional<List<Todo>> todos = todoDatabase.selectByIds(List.of(uuid, uuid1));

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertEquals(todos.get().size(), 2);
    }


}
