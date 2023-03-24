package com.tool.pomodoro.technique.tool.storage.file.todo;

import com.tool.pomodoro.technique.tool.storage.file.FileStorage;
import com.tool.pomodoro.technique.tool.storage.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.TodoCategory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileTodoCategoryStorage implements TodoCategoryStorage, FileStorage {

    private final String filePath;

    private FileTodoCategoryStorage(String filePath) {
        this.filePath = filePath;
        load();
    }

    private static FileTodoCategoryStorage fileTodoCategoryStorage;

    public static FileTodoCategoryStorage getInstance(String filePath) {
        if (Objects.isNull(fileTodoCategoryStorage)) {
            synchronized (FileTodoCategoryStorage.class) {
                if (Objects.isNull(fileTodoCategoryStorage)) {
                    fileTodoCategoryStorage = new FileTodoCategoryStorage(filePath);
                }
            }
        }
        return fileTodoCategoryStorage;
    }

    private final List<TodoCategory> dataList = new LinkedList<>();


    @Override
    public void save(TodoCategory todoCategory) {
        Optional.ofNullable(todoCategory)
                .ifPresent(data -> {
                    dataList.add(data);
                    store();
                });
    }

    @Override
    public void delete(String id) {
        Optional.ofNullable(id)
                .ifPresent(dataId -> {
                    dataList.removeIf(todoCategory -> todoCategory.id().equals(dataId));
                    store();
                });
    }

    @Override
    public void update(TodoCategory todoCategory) {
        Optional.ofNullable(todoCategory)
                .flatMap(item -> selectById(item.id()))
                .ifPresent(item -> {
                    doUpdate(item, todoCategory);
                    store();
                });
    }

    private void doUpdate(TodoCategory oldTodoCategory, TodoCategory newTodoCategory) {
        int index = dataList.indexOf(oldTodoCategory);
        if (index == -1) {
            return;
        }
        dataList.remove(index);
        dataList.add(index, newTodoCategory);
    }

    @Override
    public Optional<TodoCategory> selectById(String id) {
        return Optional.ofNullable(id)
                .flatMap(dataId -> dataList
                        .stream()
                        .filter(todoCategory -> todoCategory.id().equals(dataId))
                        .findFirst());
    }

    @Override
    public Optional<TodoCategory> selectByName(String name) {
        return Optional.ofNullable(name)
                .flatMap(dataName -> dataList
                        .stream()
                        .filter(todoCategory -> todoCategory.name().equals(dataName))
                        .findFirst());
    }

    @Override
    public Optional<List<TodoCategory>> selectAll() {
        return Optional.of(dataList);
    }


    private static final String FILE_NAME = "todo_category.json";

    @Override
    public void load() {
        FileUtil.getFileOrCreate(filePath + FILE_NAME)
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, TodoCategory.class)));
    }

    @Override
    public void store() {
        FileUtil.getFileOrCreate(filePath + FILE_NAME)
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }
}
