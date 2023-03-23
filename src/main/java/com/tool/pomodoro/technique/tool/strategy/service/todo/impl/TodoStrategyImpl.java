package com.tool.pomodoro.technique.tool.strategy.service.todo.impl;

import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.wrapper.TodoStrategyWrapper;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryLinkTodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.Todo;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class TodoStrategyImpl implements TodoStrategy {

    private final TodoStorage todoStorage;
    private final TodoCategoryLinkTodoStorage linkStorage;
    private final TodoCategoryStrategy todoCategoryStrategy;

    public TodoStrategyImpl(TodoStorage todoStorage, TodoCategoryStrategy todoCategoryStrategy, TodoCategoryLinkTodoStorage linkStorage) {
        this.todoStorage = todoStorage;
        this.linkStorage = linkStorage;
        this.todoCategoryStrategy = todoCategoryStrategy;
    }

    @Override
    public String add(TodoAddDto dto) {
        return Optional.ofNullable(dto)
                .filter(addDto -> Objects.nonNull(addDto.todo()) && Objects.nonNull(addDto.todoCategoryId())
                        && !addDto.todo().isBlank() && !addDto.todo().isBlank())
                .map(addDto -> {
                    Todo todo = new Todo(IdUtil.generate(), dto.todo());
                    todoStorage.save(todo);
                    linkStorage.save(dto.todoCategoryId(), todo.id());
                    return todo.id();
                }).orElseThrow(() -> new IllegalArgumentException("The parameter is incorrect"));
    }

    @Override
    public void delete(String id) {
        todoStorage.deleteById(id);
        linkStorage.deleteByTodoId(id);
    }

    @Override
    public void update(TodoUpdateDto dto) {
        var todo = new Todo(dto.id(), dto.content());
        todoStorage.update(todo);
    }

    @Override
    public void moveCategory(String id, String categoryId) {
        Optional.ofNullable(id)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(todo -> Optional.ofNullable(categoryId)
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(category -> {
                            linkStorage.deleteByTodoId(todo);
                            linkStorage.save(category, todo);
                        }));
    }

    @Override
    public Optional<TodoDto> get(String id) {
        return Optional.ofNullable(id)
                .filter(Predicate.not(String::isBlank))
                .flatMap(todoId -> todoStorage.selectById(todoId)
                        .map(todo -> new TodoDto(todo.id(), todo.content(), todo.createTime())));
    }

    @Override
    public Optional<TodoCategoryDto> getCategory(String id) {
        return Optional.ofNullable(id)
                .filter(Predicate.not(String::isBlank))
                .flatMap(linkStorage::selectTodoCategoryId)
                .flatMap(todoCategoryStrategy::get);
    }

    @Override
    public Optional<List<TodoDto>> all() {
        return todoStorage.selectAll()
                .map(TodoStrategyWrapper::wrapTodoDtoList);
    }

    @Override
    public Optional<List<TodoDto>> listByCategory(String categoryId) {
        return Optional.ofNullable(categoryId)
                .filter(Predicate.not(String::isBlank))
                .flatMap(linkStorage::selectTodoIds)
                .flatMap(todoStorage::selectByIds)
                .map(TodoStrategyWrapper::wrapTodoDtoList);
    }


}
