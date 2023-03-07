package com.tool.pomodoro.technique.tool.strategy.service.todo;

import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;

import java.util.List;
import java.util.Optional;

public interface TodoStrategy {
    String add(TodoAddDto dto);

    void update(TodoUpdateDto updateDto);

    void delete(String id);

    Optional<TodoDto> get(String id);

    Optional<List<TodoDto>> all();
}
