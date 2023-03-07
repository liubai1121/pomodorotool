package com.tool.pomodoro.technique.tool.database.file.todo;

import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.init.ToolInit;
import com.tool.pomodoro.technique.tool.strategy.database.todo.TodoDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.todo.po.Todo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FileTodoDatabase implements TodoDatabase, ToolInit {

    List<Todo> data = new LinkedList<>();

    @Override
    public void init() {
        data.clear();
        load();
    }

    @Override
    public void save(Todo todo) {
        Optional.ofNullable(todo)
                .ifPresent(item -> {
                    data.add(item);
                    store();
                });
    }

    @Override
    public void saveBatch(List<Todo> todo) {
        Optional.ofNullable(todo)
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(list -> {
                    data.addAll(list);
                    store();
                });
    }

    @Override
    public void deleteById(String uuid) {
        Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    data.removeIf(item -> item.getId().equals(id));
                    store();
                });
    }

    @Override
    public void update(Todo todo) {
        Optional.ofNullable(todo)
                .flatMap(item -> selectById(item.getId()))
                .ifPresent(item -> {
                    item.setContent(todo.getContent());
                    store();
                });
    }

    @Override
    public Optional<Todo> selectById(String todoId) {
        return Optional.ofNullable(todoId)
                .flatMap(id -> data.stream()
                        .filter(todo -> todo.getId().equals(id))
                        .findFirst());
    }

    @Override
    public Optional<List<Todo>> selectAll() {
        return Optional.ofNullable(data)
                .filter(Predicate.not(Collection::isEmpty));
    }

    private void store() {
        FileUtil.getTodoFile()
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }


    private void load() {
        FileUtil.getTodoFile()
                .filter(file -> file.length() > 0)
                .ifPresent(file -> {
                    var todos = FileUtil.doDeserialized(file, Todo.class);
                    data.addAll(todos);
                });
    }
}
