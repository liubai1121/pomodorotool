package com.tool.pomodoro.technique.tool.database.file.label;

import com.tool.pomodoro.technique.tool.strategy.database.label.LabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileLabelDatabaseTests {

    private final LabelDatabase fileLabelDatabase = new FileLabelDatabase();

    @Test
    void save() {
        String labelId = UUID.randomUUID().toString();
        String labelName = "test save";
        var label = new Label();
        label.setLabelId(labelId);
        label.setLabelName(labelName);

        fileLabelDatabase.save(label);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().getLabelId());
        Assertions.assertEquals(labelName, labelOpt.get().getLabelName());
    }

    @Test
    void delete() {
        String labelId = UUID.randomUUID().toString();
        String labelName = "test delete";
        var label = new Label();
        label.setLabelId(labelId);
        label.setLabelName(labelName);

        fileLabelDatabase.save(label);

        fileLabelDatabase.delete(labelId);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isEmpty());
    }

    @Test
    void update() {
        String labelId = UUID.randomUUID().toString();
        String labelName = "test update";
        var label = new Label();
        label.setLabelId(labelId);
        label.setLabelName(labelName);

        fileLabelDatabase.save(label);

        var updateName = "test update success!";
        var updateLabel = new Label();
        updateLabel.setLabelId(labelId);
        updateLabel.setLabelName(updateName);
        fileLabelDatabase.update(updateLabel);

        Optional<Label> labelOpt = fileLabelDatabase.selectById(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(updateName, labelOpt.get().getLabelName());
    }

    @Test
    void selectByName() {
        String labelId = UUID.randomUUID().toString();
        var label = new Label();
        label.setLabelId(labelId);
        label.setLabelName("test selectByName");

        fileLabelDatabase.save(label);

        Optional<Label> labelOpt = fileLabelDatabase.selectByName("test selectByName");

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(labelId, labelOpt.get().getLabelId());
    }

    @Test
    void selectByIds() {
        String labelId = UUID.randomUUID().toString();
        var label = new Label();
        label.setLabelId(labelId);
        label.setLabelName("test selectByIds1");

        String labelId1 = UUID.randomUUID().toString();
        var label1 = new Label();
        label1.setLabelId(labelId1);
        label1.setLabelName("test selectByIds2");

        fileLabelDatabase.save(label);
        fileLabelDatabase.save(label1);

        Optional<List<Label>> labelsOpt = fileLabelDatabase.selectByIds(List.of(labelId, labelId1));

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(2, labelsOpt.get().size());
    }

    @Test
    void selectAll() {
        var label = new Label();
        label.setLabelId(UUID.randomUUID().toString());
        label.setLabelName("test selectAll");

        fileLabelDatabase.save(label);

        Optional<List<Label>> labelsOpt = fileLabelDatabase.selectAll();

        Assertions.assertTrue(labelsOpt.isPresent());
    }
}
