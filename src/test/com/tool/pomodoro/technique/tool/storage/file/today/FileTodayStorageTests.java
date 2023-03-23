package com.tool.pomodoro.technique.tool.storage.file.today;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FileTodayStorageTests {

    FileTodayStorage todayStorage = FileTodayStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void save() {
        doSave();

        Optional<List<Today>> todayList = todayStorage.selectAll();

        Assertions.assertTrue(todayList.isPresent());
        Assertions.assertFalse(todayList.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var today = new Today(IdUtil.generate(), "test saveBatch1", "test category");

        var today1 = new Today(IdUtil.generate(), "test saveBatch2", "test category");

        todayStorage.saveBatch(List.of(today, today1));

        Optional<List<Today>> todos = todayStorage.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void delete() {
        String id = doSave();

        Assertions.assertTrue(todayStorage.selectById(id).isPresent());

        todayStorage.deleteById(id);

        Assertions.assertFalse(todayStorage.selectById(id).isPresent());
    }

    @Test
    void update() {
        String id = doSave();

        String updateContent = "update success!";
        String updateCategory = "update category!";

        var updateToday = new Today(id, updateContent, updateCategory);
        todayStorage.update(updateToday);

        Optional<Today> updatedToday = todayStorage.selectById(id);
        Assertions.assertTrue(updatedToday.isPresent());
        Assertions.assertEquals(updatedToday.get().content(), updateContent);
        Assertions.assertEquals(updatedToday.get().category(), updateCategory);
    }

    @Test
    void selectById() {
        String id = doSave();

        Optional<Today> todayOpt = todayStorage.selectById(id);

        Assertions.assertTrue(todayOpt.isPresent());
    }

    @Test
    void selectAll() {
        doSave();

        Optional<List<Today>> todayOpt = todayStorage.selectAll();

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDay() {
        doSave();

        var today = LocalDate.now();
        Optional<List<Today>> todayOpt = todayStorage.getByDay(today);

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDuration() {
        doSave();

        var today = LocalDate.now();
        var weekAgo = today.minusWeeks(1);
        Optional<List<Today>> todayOpt = todayStorage.getByDuration(weekAgo, today);

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDurationForStartGreaterEnd() {
        doSave();

        var today = LocalDate.now();
        var weekAgo = today.minusWeeks(1);
        Optional<List<Today>> todayOpt = todayStorage.getByDuration(today, weekAgo);

        Assertions.assertTrue(todayOpt.isEmpty());
    }

    private String doSave() {
        var random = new Random();
        var idx = random.nextInt(10000);
        var id = IdUtil.generate();
        todayStorage.save(new Today(id , "doSave" + idx, "category"));
        return id;
    }
}
