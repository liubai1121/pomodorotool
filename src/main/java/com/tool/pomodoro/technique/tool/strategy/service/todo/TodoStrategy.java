package com.tool.pomodoro.technique.tool.strategy.service.todo;

import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;

import java.util.List;
import java.util.Optional;

public interface TodoStrategy {
    String add(TodoAddDto dto);

    void delete(String id);

    void update(TodoUpdateDto updateDto);

    void moveCategory(String id, String categoryId);

    Optional<TodoDto> get(String id);

    Optional<TodoCategoryDto> getCategory(String id);

    Optional<List<TodoDto>> all();

    Optional<List<TodoDto>> listByCategory(String categoryId);
}
