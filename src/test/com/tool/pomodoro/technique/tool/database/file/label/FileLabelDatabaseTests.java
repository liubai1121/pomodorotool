package com.tool.pomodoro.technique.tool.database.file.label;

import com.tool.pomodoro.technique.tool.strategy.database.label.LabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileLabelDatabaseTests {

    private final LabelDatabase fileLabelDatabase = new FileLabelDatabase();

    @Test
    void save() {
        String labelId = IdUtil.generate();
        String labelName = "test save";
        var label = new Label(labelId, labelName);

        fileLabelDatabase.save(label);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().labelId());
        Assertions.assertEquals(labelName, labelOpt.get().labelName());
    }

    @Test
    void delete() {
        String labelId = IdUtil.generate();
        String labelName = "test delete";
        var label = new Label(labelId, labelName);

        fileLabelDatabase.save(label);

        fileLabelDatabase.delete(labelId);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isEmpty());
    }

    @Test
    void update() {
        String labelId = IdUtil.generate();
        String labelName = "test update";
        var label = new Label(labelId, labelName);

        fileLabelDatabase.save(label);

        var updateName = "test update success!";
        var updateLabel = new Label(labelId, updateName);
        fileLabelDatabase.update(updateLabel);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(updateName, labelOpt.get().labelName());
    }

    @Test
    void selectByName() {
        String labelId = IdUtil.generate();
        var label = new Label(labelId, "test selectByName");

        fileLabelDatabase.save(label);

        Optional<Label> labelOpt = fileLabelDatabase.selectByName("test selectByName");

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().labelId());
    }

    @Test
    void selectByIds() {
        String labelId = IdUtil.generate();
        var label = new Label(labelId, "test selectByIds1");

        String labelId1 = IdUtil.generate();
        var label1 = new Label(labelId1, "test selectByIds2");

        fileLabelDatabase.save(label);
        fileLabelDatabase.save(label1);

        Optional<List<Label>> labelsOpt = fileLabelDatabase.selectByIds(List.of(labelId, labelId1));

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(2, labelsOpt.get().size());
    }

    @Test
    void selectAll() {
        var label = new Label(IdUtil.generate(), "test selectAll");

        fileLabelDatabase.save(label);

        Optional<List<Label>> labelsOpt = fileLabelDatabase.selectAll();

        Assertions.assertTrue(labelsOpt.isPresent());
    }
}
