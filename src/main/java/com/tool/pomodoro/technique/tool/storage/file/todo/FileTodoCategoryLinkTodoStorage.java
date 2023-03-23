package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.FileStorage;
import com.tool.pomodoro.technique.tool.storage.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryLinkTodoStorage;

import java.util.*;
import java.util.function.Predicate;

public class FileTodoCategoryLinkTodoStorage implements TodoCategoryLinkTodoStorage, FileStorage {

    private final String filePath;

    private FileTodoCategoryLinkTodoStorage(String filePath) {
        this.filePath = filePath;
        load();
    }

    private static FileTodoCategoryLinkTodoStorage fileTodoCategoryLinkTodoStorage;

    public static FileTodoCategoryLinkTodoStorage getInstance(String filePath) {
        if (Objects.isNull(fileTodoCategoryLinkTodoStorage)) {
            synchronized (FileTodoCategoryLinkTodoStorage.class) {
                if (Objects.isNull(fileTodoCategoryLinkTodoStorage)) {
                    fileTodoCategoryLinkTodoStorage = new FileTodoCategoryLinkTodoStorage(filePath);
                }
            }
        }
        return fileTodoCategoryLinkTodoStorage;
    }

    private final Map<String, List<String>> link = new HashMap<>();
    private final Map<String, String> linkForTodo = new HashMap<>();

    @Override
    public void save(String todoCategoryId, String todoId) {
        Optional.ofNullable(todoCategoryId)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(todoCategory -> Optional.ofNullable(todoId)
                        .filter(Predicate.not(String::isBlank))
                        .filter(todo -> Objects.isNull(linkForTodo.get(todo)))
                        .ifPresent(todo -> {
                            linkForTodo.put(todo, todoCategory);
                            List<String> strings = link.computeIfAbsent(todoCategory, key -> new ArrayList<>());
                            strings.add(todo);
                            store();
                        }));
    }

    @Override
    public void delete(String todoCategoryId, String todoId) {
        Optional.ofNullable(todoCategoryId)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(todoCategory -> Optional.ofNullable(todoId)
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(todo -> {
                            String categoryId = linkForTodo.get(todo);
                            boolean categoryNotMatch = !todoCategory.equals(categoryId);
                            if (categoryNotMatch) {
                                return;
                            }

                            linkForTodo.remove(todo);
                            List<String> todoIds = link.get(todoCategory);
                            todoIds.remove(todo);

                            store();
                        }));
    }

    @Override
    public void deleteByTodoId(String todoId) {
        Optional.ofNullable(todoId)
                .ifPresent(todo -> {
                    String todoCategoryId = linkForTodo.get(todo);
                    if (Objects.nonNull(todoCategoryId) && !todoCategoryId.isBlank()) {
                        delete(todoCategoryId, todo);
                    }
                });
    }

    @Override
    public void deleteByCategoryId(String todoCategoryId) {
        Optional.ofNullable(todoCategoryId)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(todoCategory -> {
                    List<String> todoIds = link.getOrDefault(todoCategory, Collections.emptyList());
                    todoIds.forEach(linkForTodo::remove);
                    link.remove(todoCategory);
                    store();
                });
    }

    @Override
    public Optional<List<String>> selectTodoIds(String todoCategoryId) {
        return Optional.ofNullable(todoCategoryId)
                .filter(Predicate.not(String::isBlank))
                .map(todoCategory -> link.getOrDefault(todoCategory, Collections.emptyList()));
    }

    @Override
    public Optional<String> selectTodoCategoryId(String todoId) {
        return Optional.ofNullable(todoId)
                .filter(Predicate.not(String::isBlank))
                .map(linkForTodo::get);
    }

    private static final String LINK_FILE_NAME = "todo-category_link_todo.json";
    private static final String LINK_FOR_TODO_FILE_NAME = "todo-category_link_todo_for_todo.json";

    @Override
    public void load() {
        FileUtil.getFileOrCreate(filePath + LINK_FILE_NAME)
                .filter(file -> file.length() > 0)
                .ifPresent(file -> this.link.putAll(FileUtil.doDeserializedLink(file)));

        FileUtil.getFileOrCreate(filePath + LINK_FOR_TODO_FILE_NAME)
                .filter(file -> file.length() > 0)
                .ifPresent(file -> this.linkForTodo.putAll(FileUtil.doDeserializedLinkForTodo(file)));
    }

    @Override
    public void store() {
        FileUtil.getFileOrCreate(filePath + LINK_FILE_NAME)
                .ifPresent(file -> FileUtil.doSerialized(file, link));

        FileUtil.getFileOrCreate(filePath + LINK_FOR_TODO_FILE_NAME)
                .ifPresent(file -> FileUtil.doSerialized(file, linkForTodo));

    }
}
