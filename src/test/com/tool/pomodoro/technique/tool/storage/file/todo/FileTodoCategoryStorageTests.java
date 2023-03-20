package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.TodoCategory;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileTodoCategoryStorageTests {

    private final TodoCategoryStorage todoCategoryStorage =
            FileTodoCategoryStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void save() {
        var id = IdUtil.generate();
        var name = "test name";
        var addTodoCategory = new TodoCategory(id, name);
        todoCategoryStorage.save(addTodoCategory);

        var todoCategory = todoCategoryStorage.selectById(id);

        Assertions.assertTrue(todoCategory.isPresent());
        Assertions.assertEquals(todoCategory.get().name(), name);
        Assertions.assertEquals(todoCategory.get().id(), id);
    }


    @Test
    void delete() {
        String id = add("test delete");

        var todoCategory = todoCategoryStorage.selectById(id);
        Assertions.assertTrue(todoCategory.isPresent());
        Assertions.assertEquals(todoCategory.get().id(), id);

        todoCategoryStorage.delete(id);

        todoCategory = todoCategoryStorage.selectById(id);
        Assertions.assertTrue(todoCategory.isEmpty());
    }

    @Test
    void update() {
        var id = IdUtil.generate();
        var name = "test update";
        var addTodoCategory = new TodoCategory(id, name);
        todoCategoryStorage.save(addTodoCategory);

        var updateName = "test update success";
        todoCategoryStorage.update(new TodoCategory(id, updateName));

        var todoCategory = todoCategoryStorage.selectById(id);

        Assertions.assertTrue(todoCategory.isPresent());
        Assertions.assertEquals(todoCategory.get().name(), updateName);
        Assertions.assertEquals(todoCategory.get().id(), id);
    }

    @Test
    void selectById() {
        String id = add("test selectById");

        var todoCategory = todoCategoryStorage.selectById(id);
        Assertions.assertTrue(todoCategory.isPresent());
        Assertions.assertEquals(todoCategory.get().id(), id);
    }

    @Test
    void selectByName() {
        var name = "test selectByName";
        add(name);

        var todoCategory = todoCategoryStorage.selectByName(name);
        Assertions.assertTrue(todoCategory.isPresent());
        Assertions.assertEquals(todoCategory.get().name(), name);
    }

    @Test
    void selectAll() {
        add("test selectAll");

        var allOpt = todoCategoryStorage.selectAll();
        Assertions.assertTrue(allOpt.isPresent());
        Assertions.assertFalse(allOpt.get().isEmpty());
    }

    private String add(String name) {
        var id = IdUtil.generate();
        var addTodoCategory = new TodoCategory(id, name);
        todoCategoryStorage.save(addTodoCategory);
        return id;
    }
}
