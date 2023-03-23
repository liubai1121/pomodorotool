package com.tool.pomodoro.technique.tool.strategy.service.todo;

import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryUpdateDto;

import java.util.List;
import java.util.Optional;

public interface TodoCategoryStrategy {
    Optional<String> add(TodoCategoryAddDto addDto);

    void delete(String id);

    void update(TodoCategoryUpdateDto updateDto);

    Optional<TodoCategoryDto> get(String id);

    Optional<List<TodoCategoryDto>> all();
}
