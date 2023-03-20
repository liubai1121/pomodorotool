package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryLinkTodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.TodoCategory;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class FileTodoCategoryLinkTodoStorageTests {

    private final TodoCategoryLinkTodoStorage todoCategoryLinkTodoStorage =
            FileTodoCategoryLinkTodoStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    private final TodoCategoryStorage todoCategoryStorage =
            FileTodoCategoryStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    private final FileTodoStorage todoDatabase =
            FileTodoStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void save() {
        Pair<String, String> link = doSave();

        assertExistLink(link);
    }

    @Test
    void todoCannotHaveTwoCategories() {
        Pair<String, String> link = doSave();

        String todoId = link.getValue1();

        var todoCategoryId = IdUtil.generate();
        todoCategoryStorage.save(new TodoCategory(todoCategoryId, "test todoHasTwoCategories"));

        todoCategoryLinkTodoStorage.save(todoCategoryId, todoId);

        Optional<String> categoryId = todoCategoryLinkTodoStorage.selectTodoCategoryId(todoId);
        Assertions.assertTrue(categoryId.isPresent());
        Assertions.assertEquals(categoryId.get(), link.getValue0());
    }

    @Test
    void delete() {
        Pair<String, String> link = doSave();
        assertExistLink(link);

        todoCategoryLinkTodoStorage.delete(link.getValue0(), link.getValue1());
        assertNotExistLink(link);
    }

    @Test
    void deleteByTodoId() {
        Pair<String, String> link = doSave();
        assertExistLink(link);

        todoCategoryLinkTodoStorage.deleteByTodoId(link.getValue1());
        assertNotExistLink(link);
    }

    @Test
    void deleteByCategoryId() {
        var todoCategoryId = IdUtil.generate();
        todoCategoryStorage.save(new TodoCategory(todoCategoryId, "test todo link category deleteByTodoCategoryId"));

        var todoId = IdUtil.generate();
        todoDatabase.save(new Todo(todoId, "test todo link category deleteByTodoCategoryId"));

        var todoId1 = IdUtil.generate();
        todoDatabase.save(new Todo(todoId1, "test todo link category deleteByTodoCategoryId1"));

        todoCategoryLinkTodoStorage.save(todoCategoryId, todoId);
        todoCategoryLinkTodoStorage.save(todoCategoryId, todoId1);


        Optional<List<String>> linksOpt = todoCategoryLinkTodoStorage.selectTodoIds(todoCategoryId);
        Assertions.assertTrue(linksOpt.isPresent());
        Assertions.assertTrue(linksOpt.get().contains(todoId));
        Assertions.assertTrue(linksOpt.get().contains(todoId1));

        todoCategoryLinkTodoStorage.deleteByCategoryId(todoCategoryId);


        Optional<List<String>> deletedLinksOpt = todoCategoryLinkTodoStorage.selectTodoIds(todoCategoryId);
        Assertions.assertTrue(deletedLinksOpt.isEmpty() || deletedLinksOpt.get().isEmpty());
    }

    @Test
    void selectTodoCategoryId() {
        Pair<String, String> link = doSave();

        Optional<String> linksOpt = todoCategoryLinkTodoStorage.selectTodoCategoryId(link.getValue1());
        Assertions.assertTrue(linksOpt.isPresent());
        Assertions.assertEquals(linksOpt.get(), link.getValue0());
    }

    @Test
    void selectTodoIds() {
        Pair<String, String> link = doSave();

        Optional<List<String>> todoIdsOpt = todoCategoryLinkTodoStorage.selectTodoIds(link.getValue0());
        Assertions.assertTrue(todoIdsOpt.isPresent());
        Assertions.assertTrue(todoIdsOpt.get().contains(link.getValue1()));
    }

    private Pair<String, String> doSave() {
        Random random = new Random();
        var todoCategoryId = IdUtil.generate();
        todoCategoryStorage.save(new TodoCategory(todoCategoryId, "test doSave" + random.nextInt(10000)));

        var todoId = IdUtil.generate();
        todoDatabase.save(new Todo(todoId, "test doSave" + random.nextInt(10000)));

        todoCategoryLinkTodoStorage.save(todoCategoryId, todoId);
        return new Pair<>(todoCategoryId, todoId);
    }

    private void assertExistLink(Pair<String, String> link) {
        Optional<List<String>> todoIdsOpt = todoCategoryLinkTodoStorage.selectTodoIds(link.getValue0());
        Assertions.assertTrue(todoIdsOpt.isPresent());
        Assertions.assertTrue(todoIdsOpt.get().contains(link.getValue1()));
    }

    private void assertNotExistLink(Pair<String, String> link) {
        Optional<List<String>> todoIdsOpt = todoCategoryLinkTodoStorage.selectTodoIds(link.getValue0());
        boolean notExistLink = todoIdsOpt.isEmpty() || !todoIdsOpt.get().contains(link.getValue1());
        Assertions.assertTrue(notExistLink);
    }
}
