package com.tool.pomodoro.technique.tool.strategy.service.label;

import com.tool.pomodoro.technique.tool.factory.label.LabelStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class LabelStrategyTest {

    private final LabelStrategy labelStrategy = LabelStrategyFactory.create();

    @Test
    void add() {
        String label = "非常奇怪的测试学习标签";
        String labelId = labelStrategy.add(label);

        labelStrategy.add(label);

        Optional<LabelDto> labelOpt = labelStrategy.get(labelId);

        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(label, labelOpt.get().labelName());

        Optional<List<LabelDto>> labelsOpt = labelStrategy.fuzzyQueryByName(label);
        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(1, labelsOpt.get().size());
    }

    @Test
    void list() {
        String label1 = "工作";
        String labelId1 = labelStrategy.add(label1);

        String label2 = "阅读";
        String labelId2 = labelStrategy.add(label2);

        Optional<List<LabelDto>> labelsOpt = labelStrategy.list(List.of(labelId1, labelId2));

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(2, labelsOpt.get().size());
    }

    @Test
    void all() {
        String label = "工具";
        labelStrategy.add(label);

        Optional<List<LabelDto>> labelsOpt = labelStrategy.all();

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertNotEquals(0, labelsOpt.get().size());
    }

    @Test
    void fuzzyQueryByName() {
        String label1 = "工作";
        labelStrategy.add(label1);

        String label2 = "阅读";
        labelStrategy.add(label2);

        String label = "工具";
        labelStrategy.add(label);

        Optional<List<LabelDto>> labelsOpt = labelStrategy.fuzzyQueryByName("工");

        Assertions.assertTrue(labelsOpt.isPresent());
        Assertions.assertEquals(2, labelsOpt.get().size());
    }

    @Test
    void delete() {
        String label = "删除测试";
        String id = labelStrategy.add(label);

        labelStrategy.delete(id);

        Optional<LabelDto> labelOpt = labelStrategy.get(id);

        Assertions.assertTrue(labelOpt.isEmpty());
    }

    @Test
    void update() {
        String label = "删除测试";
        String id = labelStrategy.add(label);

        var updateName = "测试更新成功";
        var updateDto = new LabelUpdateDto(id, updateName);
        labelStrategy.update(updateDto);

        Optional<LabelDto> labelOpt = labelStrategy.get(id);
        Assertions.assertTrue(labelOpt.isPresent());
        Assertions.assertEquals(updateName, labelOpt.get().labelName());
    }
}
