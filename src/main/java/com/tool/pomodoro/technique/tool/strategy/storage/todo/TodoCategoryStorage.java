package com.tool.pomodoro.technique.tool.strategy.storage.todo;

import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.TodoCategory;

import java.util.List;
import java.util.Optional;

public interface TodoCategoryStorage {

    void save(TodoCategory todoCategory);

    void delete(String id);

    void update(TodoCategory todoCategory);

    Optional<TodoCategory> selectById(String id);

    Optional<TodoCategory> selectByName(String name);

    Optional<List<TodoCategory>> selectAll();
}
