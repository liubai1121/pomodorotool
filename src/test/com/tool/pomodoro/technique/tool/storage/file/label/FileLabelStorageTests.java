package com.tool.pomodoro.technique.tool.storage.file.label;

import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.storage.label.LabelStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.label.po.Label;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class FileLabelStorageTests {

    private final LabelStorage fileLabelStorage =
            FileLabelStorage.getInstance(new TestFilePathConfig().getStoreFilesPath());

    @Test
    void save() {
        String labelId = IdUtil.generate();
        String labelName = "test save";
        var label = new Label(labelId, labelName);

        fileLabelStorage.save(label);

        Optional<Label> labelOpt = fileLabelStorage.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().labelId());
        Assertions.assertEquals(labelName, labelOpt.get().labelName());
    }

    @Test
    void delete() {
        String labelId = IdUtil.generate();
        String labelName = "test delete";
        var label = new Label(labelId, labelName);

        fileLabelStorage.save(label);

        fileLabelStorage.delete(labelId);

        Optional<Label> labelOpt = fileLabelStorage.selectById(labelId);

        Assertions.assertTrue(labelOpt.isEmpty());
    }

    @Test
    void update() {
        String labelId = IdUtil.generate();
        String labelName = "test update";
        var label = new Label(labelId, labelName);

        fileLabelStorage.save(label);

        var updateName = "test update success!";
        var updateLabel = new Label(labelId, updateName);
        fileLabelStorage.update(updateLabel);

        Optional<Label> labelOpt = fileLabelStorage.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(updateName, labelOpt.get().labelName());
    }

    @Test
    void selectByName() {
        String labelId = IdUtil.generate();
        var label = new Label(labelId, "test selectByName");

        fileLabelStorage.save(label);

        Optional<Label> labelOpt = fileLabelStorage.selectByName("test selectByName");

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().labelId());
    }

    @Test
    void selectByIds() {
        String labelId = IdUtil.generate();
        var label = new Label(labelId, "test selectByIds1");

        String labelId1 = IdUtil.generate();
        var label1 = new Label(labelId1, "test selectByIds2");

        fileLabelStorage.save(label);
        fileLabelStorage.save(label1);

        Optional<List<Label>> labelsOpt = fileLabelStorage.selectByIds(List.of(labelId, labelId1));

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(2, labelsOpt.get().size());
    }

    @Test
    void selectAll() {
        var label = new Label(IdUtil.generate(), "test selectAll");

        fileLabelStorage.save(label);

        Optional<List<Label>> labelsOpt = fileLabelStorage.selectAll();

        Assertions.assertTrue(labelsOpt.isPresent());
    }
}
