package com.tool.pomodoro.technique.tool.storage.memory.todo;

import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MemoryTodoStorage implements TodoStorage {

    private List<Todo> list = new ArrayList<>();

    @Override
    public void save(Todo todo) {
        Optional.ofNullable(todo)
                .ifPresent(list::add);
    }

    @Override
    public void saveBatch(List<Todo> todos) {
        Optional.ofNullable(todos)
                .filter(Predicate.not(List::isEmpty))
                .ifPresent(list::addAll);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void update(Todo todo) {

    }

    @Override
    public Optional<Todo> selectById(String todoId) {
        return Optional.ofNullable(todoId)
                .flatMap(id -> list.stream()
                        .filter(todo -> todo.id().equals(id))
                        .findFirst());
    }

    @Override
    public Optional<List<Todo>> selectAll() {
        return Optional.ofNullable(list);
    }

    @Override
    public Optional<List<Todo>> selectByIds(List<String> todoIds) {
        return Optional.empty();
    }
}
