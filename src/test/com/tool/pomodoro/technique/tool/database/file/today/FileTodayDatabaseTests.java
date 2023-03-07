package com.tool.pomodoro.technique.tool.database.file.today;

import com.tool.pomodoro.technique.tool.database.file.FileUtil;
import com.tool.pomodoro.technique.tool.database.file.today.FileTodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileTodayDatabaseTests {

    FileTodayDatabase database = new FileTodayDatabase();

    @BeforeEach
    void initAll() {
        database.init();
    }

    @Test
    void init() {
        Optional<File> todoFile = FileUtil.getTodayFile()
                .filter(file -> file.length() > 0);
        Optional<List<Today>> todos = database.selectAll();
        Assertions.assertEquals(todoFile.isPresent(), todos.isPresent());
    }

    @Test
    void save() {
        var today = new Today();
        today.setId(UUID.randomUUID().toString());
        today.setContent("test save");
        today.setCreateTime(LocalDateTime.now());
        database.save(today);

        Optional<List<Today>> todayList = database.selectAll();

        Assertions.assertTrue(todayList.isPresent());
        Assertions.assertFalse(todayList.get().isEmpty());
    }

    @Test
    void saveBatch() {
        var today = new Today();
        today.setId(UUID.randomUUID().toString());
        today.setContent("test saveBatch1");
        today.setCreateTime(LocalDateTime.now());

        var today1 = new Today();
        today1.setId(UUID.randomUUID().toString());
        today1.setContent("test saveBatch2");
        today1.setCreateTime(LocalDateTime.now());

        database.saveBatch(List.of(today, today1));

        Optional<List<Today>> todos = database.selectAll();

        Assertions.assertTrue(todos.isPresent());
        Assertions.assertFalse(todos.get().isEmpty());
    }

    @Test
    void delete() {
        String id = UUID.randomUUID().toString();
        var today = new Today();
        today.setId(id);
        today.setContent("test delete");
        today.setCreateTime(LocalDateTime.now());
        database.save(today);

        Assertions.assertTrue(database.selectById(id).isPresent());

        database.deleteById(id);

        Assertions.assertFalse(database.selectById(id).isPresent());
    }

    @Test
    void update() {
        String id = UUID.randomUUID().toString();
        var today = new Today();
        today.setId(id);
        today.setContent("test update");
        today.setCreateTime(LocalDateTime.now());
        database.save(today);

        String updateContent = "update success!";
        var updateToday = new Today();
        updateToday.setId(id);
        updateToday.setContent(updateContent);
        database.update(updateToday);

        Optional<Today> updatedToday = database.selectById(id);
        Assertions.assertTrue(updatedToday.isPresent());
        Assertions.assertEquals(updatedToday.get().getContent(), updateContent);
    }

    @Test
    void selectById() {
        String uuid = UUID.randomUUID().toString();
        var today = new Today();
        today.setId(uuid);
        today.setContent("test selectById");
        today.setCreateTime(LocalDateTime.now());
        database.save(today);

        Optional<Today> todayOpt = database.selectById(uuid);

        Assertions.assertTrue(todayOpt.isPresent());
    }

    @Test
    void selectAll() {
        var today = new Today();
        today.setId(UUID.randomUUID().toString());
        today.setContent("test selectAll");
        today.setCreateTime(LocalDateTime.now());
        database.save(today);

        Optional<List<Today>> todayOpt = database.selectAll();

        Assertions.assertTrue(todayOpt.isPresent());
        Assertions.assertFalse(todayOpt.get().isEmpty());
    }



}
