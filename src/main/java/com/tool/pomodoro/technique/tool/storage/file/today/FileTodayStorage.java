package com.tool.pomodoro.technique.tool.storage.file.today;

import com.tool.pomodoro.technique.tool.storage.file.FileStorage;
import com.tool.pomodoro.technique.tool.storage.file.FileUtil;
import com.tool.pomodoro.technique.tool.strategy.storage.today.TodayStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileTodayStorage implements TodayStorage, FileStorage {
    private final String filePath;

    private FileTodayStorage(String filePath) {
        this.filePath = filePath;
        load();
    }

    private static FileTodayStorage fileTodayStorage;

    public static FileTodayStorage getInstance(String filePath) {
        if (Objects.isNull(fileTodayStorage)) {
            synchronized (FileTodayStorage.class) {
                if (Objects.isNull(fileTodayStorage)) {
                    fileTodayStorage = new FileTodayStorage(filePath);
                }
            }
        }
        return fileTodayStorage;
    }

    private final List<Today> dataList = new LinkedList<>();

    @Override
    public void load() {
        FileUtil.getFileOrCreate(getFileName())
                .filter(file -> file.length() > 0)
                .ifPresent(file -> dataList.addAll(FileUtil.doDeserialized(file, Today.class)));
    }

    @Override
    public void store() {
        FileUtil.getFileOrCreate(getFileName())
                .ifPresent(file -> selectAll().ifPresent(list -> FileUtil.doSerialized(file, list)));
    }

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
                .flatMap(item -> selectById(item.id()))
                .ifPresent(item -> {
                    doUpdate(item, today);
                    store();
                });
    }

    private void doUpdate(Today oldToday, Today newToday) {
        int index = dataList.indexOf(oldToday);
        if (index == -1) {
            return;
        }
        dataList.remove(index);
        dataList.add(index, newToday);
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
    public Optional<List<Today>> getByDay(LocalDate date) {
        return Optional.ofNullable(date)
                .map(this::getDateFileName)
                .map(File::new)
                .filter(file -> file.exists() && file.length() > 0)
                .map(file -> FileUtil.doDeserialized(file, Today.class));
    }

    @Override
    public Optional<List<Today>> getByDuration(LocalDate startDay, LocalDate endDay) {
        if (Objects.isNull(startDay) || Objects.isNull(endDay)) {
            return Optional.empty();
        }

        if (startDay.isAfter(endDay)) {
            return Optional.empty();
        }

        List<Today> result = new ArrayList<>();
        for (LocalDate day = startDay;
             day.isBefore(endDay) || day.isEqual(endDay);
             day = day.plusDays(1)) {
            getByDay(day).ifPresent(result::addAll);
        }

        return Optional.of(result);
    }

    private String getFileName() {
        return getDateFileName(LocalDate.now());
    }

    private String getDateFileName(LocalDate localDate) {
        String format = DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
        return filePath + format + ".json";
    }

}
