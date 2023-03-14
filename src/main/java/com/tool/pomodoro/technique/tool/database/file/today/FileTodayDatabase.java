package com.tool.pomodoro.technique.tool.database.file.today;

import com.tool.pomodoro.technique.tool.database.file.FileBaseDatabase;
import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.database.today.TodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileTodayDatabase implements TodayDatabase, FileBaseDatabase {

    public FileTodayDatabase() {
        load();
    }

    private final List<Today> dataList = new LinkedList<>();

    @Override
    public void save(Today today) {
        Optional.ofNullable(today)
                .ifPresent(item -> {
                    dataList.add(item);
                    store();
                });
    }

    @Override
    public void saveBatch(List<Today> today) {
        Optional.ofNullable(today)
                .map(list -> list.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(list -> {
                    dataList.addAll(list);
                    store();
                });
    }

    @Override
    public void deleteById(String uuid) {
        Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    dataList.removeIf(item -> item.id().equals(id));
                    store();
                });
    }

    @Override
    public void update(Today today) {
        Optional.ofNullable(today)
                .ifPresent(item -> {
                    doUpdate(item);
                    store();
                });
    }

    private void doUpdate(Today today) {
        int index = dataList.indexOf(today);
        if (index == -1) {
            return;
        }
        dataList.remove(index);
        dataList.add(index, today);
    }

    @Override
    public Optional<Today> selectById(String uuid) {
        return selectAll()
                .flatMap(list -> list.stream()
                        .filter(item -> item.id().equals(uuid))
                        .findFirst());
    }

    @Override
    public Optional<List<Today>> selectAll() {
        return Optional.of(dataList)
                .map(this::filterNullObject)
                .map(Collections::unmodifiableList);
    }

    private <T> List<T> filterNullObject(List<T> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void load() {
        FileUtil.getTodayFile()
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, Today.class)));
    }

    @Override
    public void store() {
        FileUtil.getTodayFile()
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }
}
