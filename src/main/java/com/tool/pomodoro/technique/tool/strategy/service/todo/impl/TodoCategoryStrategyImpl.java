package com.tool.pomodoro.technique.tool.strategy.service.todo.impl;

import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryLinkTodoStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.TodoCategoryStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.todo.po.TodoCategory;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class TodoCategoryStrategyImpl implements TodoCategoryStrategy {

    private final TodoCategoryStorage todoCategoryStorage;
    private final TodoCategoryLinkTodoStorage linkStorage;

    public TodoCategoryStrategyImpl(TodoCategoryStorage todoCategoryStorage, TodoCategoryLinkTodoStorage linkStorage) {
        this.todoCategoryStorage = todoCategoryStorage;
        this.linkStorage = linkStorage;
    }

    @Override
    public Optional<String> add(TodoCategoryAddDto addDto) {
        return Optional.ofNullable(addDto)
                .map(TodoCategoryAddDto::name)
                .flatMap(todoCategoryStorage::selectByName)
                .map(TodoCategory::id)
                .or(() -> this.doAdd(addDto));
    }

    private Optional<String> doAdd(TodoCategoryAddDto addDto) {
        return Optional.ofNullable(addDto)
                .map(TodoCategoryAddDto::name)
                .filter(Predicate.not(String::isBlank))
                .map(name -> {
                    var id = IdUtil.generate();
                    var todoCategory = new TodoCategory(id, name);
                    todoCategoryStorage.save(todoCategory);
                    return id;
                });
    }

    @Override
    public void delete(String id) {
        Optional.ofNullable(id)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(category -> {
                    todoCategoryStorage.delete(category);
                    linkStorage.deleteByCategoryId(category);
                });
    }

    @Override
    public void update(TodoCategoryUpdateDto updateDto) {
        Optional.ofNullable(updateDto)
                .filter(dto -> Objects.nonNull(dto.id()) && Objects.nonNull(updateDto.name()) && !updateDto.name().isBlank())
                .ifPresent(dto -> {
                    var todoCategory = new TodoCategory(dto.id(), dto.name());
                    todoCategoryStorage.update(todoCategory);
                });
    }

    @Override
    public Optional<TodoCategoryDto> get(String id) {
        return Optional.ofNullable(id)
                .filter(Predicate.not(String::isBlank))
                .flatMap(todoCategoryStorage::selectById)
                .map(todoCategory -> new TodoCategoryDto(todoCategory.id(), todoCategory.name()));
    }

    @Override
    public Optional<List<TodoCategoryDto>> all() {
        return todoCategoryStorage.selectAll()
                .filter(Predicate.not(Collection::isEmpty))
                .map(todoCategories -> todoCategories.stream()
                        .filter(Objects::nonNull)
                        .map(todoCategory -> new TodoCategoryDto(todoCategory.id(), todoCategory.name()))
                        .toList());
    }
}
