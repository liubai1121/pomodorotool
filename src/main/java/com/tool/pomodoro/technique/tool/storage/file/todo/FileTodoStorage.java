package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.FileStorage;
import com.tool.pomodoro.technique.tool.storage.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;

import java.util.*;
import java.util.function.Predicate;

public class FileTodoStorage implements TodoStorage, FileStorage {

    private final String filePath;

    private FileTodoStorage(String filePath) {
        this.filePath = filePath;
        load();
    }

    private static FileTodoStorage fileTodoStorage;

    public static FileTodoStorage getInstance(String filePath) {
        if (Objects.isNull(fileTodoStorage)) {
            synchronized (FileTodoStorage.class) {
                if (Objects.isNull(fileTodoStorage)) {
                    fileTodoStorage = new FileTodoStorage(filePath);
                }
            }
        }
        return fileTodoStorage;
    }

    private final static String FILE_NAME = "todo.json";

    private final List<Todo> dataList = new LinkedList<>();

    @Override
    public void save(Todo todo) {
        Optional.ofNullable(todo)
                .ifPresent(item -> {
                    dataList.add(item);
                    store();
                });
    }

    @Override
    public void saveBatch(List<Todo> todo) {
        Optional.ofNullable(todo)
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(list -> {
                    dataList.addAll(list);
                    store();
                });
    }

    @Override
    public void deleteById(String uuid) {
        Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    dataList.removeIf(item -> item.id().equals(id));
                    store();
                });
    }

    @Override
    public void update(Todo todo) {
        Optional.ofNullable(todo)
                .flatMap(item -> selectById(item.id()))
                .ifPresent(item -> {
                    doUpdate(item, todo);
                    store();
                });
    }

    private void doUpdate(Todo oldTodo, Todo newTodo) {
        int index = dataList.indexOf(oldTodo);
        if (index == -1) {
            return;
        }
        dataList.remove(index);
        dataList.add(index, newTodo);
    }

    @Override
    public Optional<Todo> selectById(String todoId) {
        return Optional.ofNullable(todoId)
                .flatMap(id -> dataList.stream()
                        .filter(todo -> todo.id().equals(id))
                        .findFirst());
    }

    @Override
    public Optional<List<Todo>> selectAll() {
        return Optional.ofNullable(dataList)
                .map(Collections::unmodifiableList);
    }

    @Override
    public void load() {
        FileUtil.getFileOrCreate(filePath + FILE_NAME)
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, Todo.class)));
    }

    @Override
    public void store() {
        FileUtil.getFileOrCreate(filePath + FILE_NAME)
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }
}
