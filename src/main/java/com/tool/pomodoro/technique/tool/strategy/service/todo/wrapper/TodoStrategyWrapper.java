package com.tool.pomodoro.technique.tool.strategy.service.todo.wrapper;

import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TodoStrategyWrapper {

    private TodoStrategyWrapper() {
    }

    public static List<TodoDto> wrapTodoDtoList(List<Todo> todoList) {
        return todoList.stream()
                .filter(Objects::nonNull)
                .map(todo -> new TodoDto(todo.id(), todo.content(), todo.createTime()))
                .collect(Collectors.toList());
    }
}
