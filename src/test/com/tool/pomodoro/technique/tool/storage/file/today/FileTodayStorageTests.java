package com.tool.pomodoro.technique.tool.storage.file.today;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FileTodayStorageTests {

    FileTodayStorage todayStorage = FileTodayStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void save() {
        var today = new Today(IdUtil.generate(), "test save");
        todayStorage.save(today);

        Optional<List<Today>> todayList = todayStorage.selectAll();

        Assertions.assertTrue(todayList.isPresent());
        Assertions.assertFalse(todayList.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var today = new Today(IdUtil.generate(), "test saveBatch1");

        var today1 = new Today(IdUtil.generate(), "test saveBatch2");

        todayStorage.saveBatch(List.of(today, today1));

        Optional<List<Today>> todos = todayStorage.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void delete() {
        String id = IdUtil.generate();
        var today = new Today(id, "test delete");
        todayStorage.save(today);

        Assertions.assertTrue(todayStorage.selectById(id).isPresent());

        todayStorage.deleteById(id);

        Assertions.assertFalse(todayStorage.selectById(id).isPresent());
    }

    @Test
    void update() {
        String id = IdUtil.generate();
        var today = new Today(id, "test update");
        todayStorage.save(today);

        String updateContent = "update success!";
        var updateToday = new Today(id, updateContent);
        todayStorage.update(updateToday);

        Optional<Today> updatedToday = todayStorage.selectById(id);
        Assertions.assertTrue(updatedToday.isPresent());
        Assertions.assertEquals(updatedToday.get().content(), updateContent);
    }

    @Test
    void selectById() {
        String uuid = IdUtil.generate();
        var today = new Today(uuid, "test selectById");
        todayStorage.save(today);

        Optional<Today> todayOpt = todayStorage.selectById(uuid);

        Assertions.assertTrue(todayOpt.isPresent());
    }

    @Test
    void selectAll() {
        var today = new Today(IdUtil.generate(), "test selectAll");
        todayStorage.save(today);

        Optional<List<Today>> todayOpt = todayStorage.selectAll();

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDay() {
        todayStorage.save(new Today(IdUtil.generate(), "test getByDay"));

        var today = LocalDate.now();
        Optional<List<Today>> todayOpt = todayStorage.getByDay(today);

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDuration() {
        todayStorage.save(new Today(IdUtil.generate(), "test getByDuration"));

        var today = LocalDate.now();
        var weekAgo = today.minusWeeks(1);
        Optional<List<Today>> todayOpt = todayStorage.getByDuration(weekAgo, today);

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }

    @Test
    void getByDurationForStartGreaterEnd() {
        todayStorage.save(new Today(IdUtil.generate(), "test getByDuration"));

        var today = LocalDate.now();
        var weekAgo = today.minusWeeks(1);
        Optional<List<Today>> todayOpt = todayStorage.getByDuration(today, weekAgo);

        Assertions.assertTrue(todayOpt.isEmpty());
    }
}
