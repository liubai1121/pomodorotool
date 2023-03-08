package com.tool.pomodoro.technique.tool.database.file.today;

import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.init.ToolInit;
import com.tool.pomodoro.technique.tool.strategy.database.today.TodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileTodayDatabase implements TodayDatabase {

    public FileTodayDatabase() {
        load();
    }
    private final List<Today> data = new LinkedList<>();

    @Override
    public void save(Today today) {
        Optional.ofNullable(today)
                .ifPresent(item -> {
                    data.add(item);
                    store();
                });
    }

    @Override
    public void saveBatch(List<Today> today) {
        Optional.ofNullable(today)
                .map(list -> list.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(list -> {
                    data.addAll(list);
                    store();
                });
    }

    @Override
    public void deleteById(String uuid) {
        Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    data.removeIf(item -> item.getId().equals(id));
                    store();
                });
    }

    @Override
    public void update(Today today) {
        Optional.ofNullable(today)
                .flatMap(item -> selectById(item.getId()))
                .ifPresent(item -> {
                    item.setContent(today.getContent());
                    item.setClocks(today.getClocks());
                    store();
                });
    }

    @Override
    public Optional<Today> selectById(String uuid) {
        return selectAll()
                .flatMap(list -> list.stream()
                        .filter(item -> item.getId().equals(uuid))
                        .findFirst());
    }

    @Override
    public Optional<List<Today>> selectAll() {
        return Optional.of(data)
                .map(this::filterNullObject)
                .filter(Predicate.not(Collection::isEmpty));
    }

    private <T> List<T> filterNullObject(List<T> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void store() {
        FileUtil.getTodayFile()
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }


    private void load() {
        FileUtil.getTodayFile()
                .filter(file -> file.length() > 0)
                .ifPresent(file -> {
                    var todos = FileUtil.doDeserialized(file, Today.class);
                    data.addAll(todos);
                });
    }
}
