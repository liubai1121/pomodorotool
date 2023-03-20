package com.tool.pomodoro.technique.tool.factory;

import com.tool.pomodoro.technique.tool.storage.file.FilePathConfig;
import com.tool.pomodoro.technique.tool.storage.file.label.FileLabelStorage;
import com.tool.pomodoro.technique.tool.storage.file.today.FileTodayStorage;
import com.tool.pomodoro.technique.tool.storage.file.todo.FileTodoCategoryLinkTodoStorage;
import com.tool.pomodoro.technique.tool.storage.file.todo.FileTodoCategoryStorage;
import com.tool.pomodoro.technique.tool.storage.file.todo.FileTodoStorage;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.impl.LabelStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.iml.TodayStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.impl.TodoCategoryStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.todo.impl.TodoStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl.TodoTodayMoveStrategyImpl;

public class FileStorageStrategyFactory implements StrategyFactory {
    private final FilePathConfig filePathConfig;

    public FileStorageStrategyFactory(FilePathConfig filePathConfig) {
        this.filePathConfig = filePathConfig;
    }

    @Override
    public TodoStrategy createTodoStrategy() {
        var todoStorage = FileTodoStorage.getInstance(filePathConfig.getStoreFilesPath());
        var linkStorage = FileTodoCategoryLinkTodoStorage.getInstance(filePathConfig.getStoreFilesPath());
        return new TodoStrategyImpl(todoStorage, createTodoCategoryStrategy(), linkStorage);
    }

    @Override
    public TodoCategoryStrategy createTodoCategoryStrategy() {
        var fileTodoCategoryStorage = FileTodoCategoryStorage.getInstance(filePathConfig.getStoreFilesPath());
        var linkStorage = FileTodoCategoryLinkTodoStorage.getInstance(filePathConfig.getStoreFilesPath());
        return new TodoCategoryStrategyImpl(fileTodoCategoryStorage, linkStorage);
    }

    @Override
    public TodayStrategy createTodayStrategy() {
        return new TodayStrategyImpl(FileTodayStorage.getInstance(filePathConfig.getStoreFilesPath()));
    }

    @Override
    public LabelStrategy createLabelStrategy() {
        return new LabelStrategyImpl(FileLabelStorage.getInstance(filePathConfig.getStoreFilesPath()));
    }

    @Override
    public TodoTodayMoveStrategy createTodoTodayMoveStrategy() {
        return new TodoTodayMoveStrategyImpl(createTodoStrategy(), createTodayStrategy());
    }
}
