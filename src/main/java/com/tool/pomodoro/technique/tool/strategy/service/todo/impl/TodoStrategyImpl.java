package com.tool.pomodoro.technique.tool.strategy.service.todo.impl;

import com.tool.pomodoro.technique.tool.strategy.database.todo.TodoDatabase;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.database.todo.po.Todo;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoStrategyImpl implements TodoStrategy {

    private final TodoDatabase database;

    public TodoStrategyImpl(TodoDatabase todoDatabase) {
        this.database = todoDatabase;
    }

    @Override
    public String add(TodoAddDto dto) {
        Todo todo = new Todo(IdUtil.generate(), dto.todo());
        database.save(todo);
        return todo.id();
    }

    @Override
    public void delete(String id) {
        database.deleteById(id);
    }

    @Override
    public void update(TodoUpdateDto dto) {
        var todo = new Todo(dto.id(), dto.content());
        database.update(todo);
    }

    @Override
    public Optional<TodoDto> get(String id) {
        return database.selectById(id)
                .map(todo -> new TodoDto(todo.id(), todo.content()));
    }

    @Override
    public Optional<List<TodoDto>> all() {
        return database.selectAll()
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(todo -> new TodoDto(todo.id(), todo.content()))
                        .collect(Collectors.toList()));
    }
}
