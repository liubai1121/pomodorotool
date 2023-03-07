package com.tool.pomodoro.technique.tool.strategy.database.todo;

import com.tool.pomodoro.technique.tool.strategy.database.todo.po.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoDatabase {
    void save(Todo todo);

    void saveBatch(List<Todo> todo);

    void deleteById(String id);

    void update(Todo todo);

    Optional<Todo> selectById(String todoId);

    Optional<List<Todo>> selectAll();
}
