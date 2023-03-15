package com.tool.pomodoro.technique.tool.storage.file.today;

import com.tool.pomodoro.technique.tool.storage.file.FileUtil;
import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class FileTodayStorageTests {

    FileTodayStorage database = FileTodayStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void init() {
        Optional<File> todoFile = FileUtil.getTodayFile()
                .filter(file -> file.length() > 0);
        Optional<List<Today>> todos = database.selectAll();
        Assertions.assertEquals(todoFile.isPresent(), todos.isPresent());
    }

    @Test
    void save() {
        var today = new Today(IdUtil.generate(), "test save");
        database.save(today);

        Optional<List<Today>> todayList = database.selectAll();

        Assertions.assertTrue(todayList.isPresent());
        Assertions.assertFalse(todayList.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var today = new Today(IdUtil.generate(), "test saveBatch1");

        var today1 = new Today(IdUtil.generate(), "test saveBatch2");

        database.saveBatch(List.of(today, today1));

        Optional<List<Today>> todos = database.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void delete() {
        String id = IdUtil.generate();
        var today = new Today(id, "test delete");
        database.save(today);

        Assertions.assertTrue(database.selectById(id).isPresent());

        database.deleteById(id);

        Assertions.assertFalse(database.selectById(id).isPresent());
    }

    @Test
    void update() {
        String id = IdUtil.generate();
        var today = new Today(id, "test update");
        database.save(today);

        String updateContent = "update success!";
        var updateToday = new Today(id, updateContent);
        database.update(updateToday);

        Optional<Today> updatedToday = database.selectById(id);
        Assertions.assertTrue(updatedToday.isPresent());
        Assertions.assertEquals(updatedToday.get().content(), updateContent);
    }

    @Test
    void selectById() {
        String uuid = IdUtil.generate();
        var today = new Today(uuid, "test selectById");
        database.save(today);

        Optional<Today> todayOpt = database.selectById(uuid);

        Assertions.assertTrue(todayOpt.isPresent());
    }

    @Test
    void selectAll() {
        var today = new Today(IdUtil.generate(), "test selectAll");
        database.save(today);

        Optional<List<Today>> todayOpt = database.selectAll();

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }
}
