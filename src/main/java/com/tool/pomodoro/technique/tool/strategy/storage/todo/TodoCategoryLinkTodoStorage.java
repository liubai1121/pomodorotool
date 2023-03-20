package com.tool.pomodoro.technique.tool.strategy.storage.todo;

import java.util.List;
import java.util.Optional;

public interface TodoCategoryLinkTodoStorage {
    void save(String todoCategoryId, String todoId);

    void delete(String todoCategoryId, String todoId);

    void deleteByTodoId(String todoId);

    void deleteByCategoryId(String todoCategoryId);

    Optional<List<String>> selectTodoIds(String todoCategoryId);

    Optional<String> selectTodoCategoryId(String todoId);
}
