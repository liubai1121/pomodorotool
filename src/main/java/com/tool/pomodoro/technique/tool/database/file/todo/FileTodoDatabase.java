package com.tool.pomodoro.technique.tool.database.file.todo;

import com.tool.pomodoro.technique.tool.database.file.FileBaseDatabase;
import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.database.todo.TodoDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.todo.po.Todo;

import java.util.*;
import java.util.function.Predicate;

public class FileTodoDatabase implements TodoDatabase, FileBaseDatabase {

    List<Todo> dataList = new LinkedList<>();

    public FileTodoDatabase() {
        load();
    }

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
        FileUtil.getTodoFile()
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, Todo.class)));
    }

    @Override
    public void store() {
        FileUtil.getTodoFile()
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }
}
