package com.tool.pomodoro.technique.tool.database.file.label;

import com.tool.pomodoro.technique.tool.database.file.FileBaseDatabase;
import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.database.label.LabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileLabelDatabase implements LabelDatabase, FileBaseDatabase {

    private final List<Label> dataList = new ArrayList<>();

    public FileLabelDatabase() {
        load();
    }

    @Override
    public void save(Label label) {
        Optional.ofNullable(label)
                .ifPresent(data -> {
                    dataList.add(data);
                    store();
                });
    }

    @Override
    public void delete(String uuid) {
        Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> {
                    dataList.removeIf(label -> label.labelId().equals(id));
                    store();
                });
    }

    @Override
    public void update(Label label) {
        Optional.ofNullable(label)
                .ifPresent(data -> {

                    doUpdate(data);
                    store();
                });
    }

    private void doUpdate(Label label) {
        int index = dataList.indexOf(label);
        if (index == -1) {
            return;
        }
        dataList.remove(index);
        dataList.add(index, label);
    }

    @Override
    public Optional<Label> selectById(String uuid) {
        return Optional.ofNullable(uuid)
                .filter(Predicate.not(String::isBlank))
                .flatMap(id -> select(label -> label.labelId().equals(id)));
    }

    @Override
    public Optional<Label> selectByName(String labelName) {
        return Optional.ofNullable(labelName)
                .filter(Predicate.not(String::isBlank))
                .flatMap(name -> select(label -> label.labelName().equals(name)));
    }

    private Optional<Label> select(Predicate<Label> predicate) {
        return Optional.ofNullable(predicate)
                .flatMap(filter -> dataList.stream()
                        .filter(filter)
                        .findFirst());
    }

    @Override
    public Optional<List<Label>> selectByIds(List<String> uuids) {
        return Optional.ofNullable(uuids)
                .filter(Predicate.not(Collection::isEmpty))
                .map(id -> dataList.stream()
                        .filter(label -> uuids.contains(label.labelId()))
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<Label>> selectAll() {
        return Optional.of(dataList)
                .map(Collections::unmodifiableList);
    }

    @Override
    public void load() {
        FileUtil.getLabelFile()
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, Label.class)));
    }

    @Override
    public void store() {
        FileUtil.getLabelFile()
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }
}
