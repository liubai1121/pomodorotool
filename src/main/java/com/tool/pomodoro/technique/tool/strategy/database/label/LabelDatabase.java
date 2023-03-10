package com.tool.pomodoro.technique.tool.strategy.database.label;

import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;

import java.util.List;
import java.util.Optional;

public interface LabelDatabase {
    void save(Label label);

    void delete(String labelId);

    void update(Label label);

    Optional<Label> selectById(String labelId);

    Optional<Label> selectByName(String labelName);

    Optional<List<Label>> selectByIds(List<String> labelIds);

    Optional<List<Label>> selectAll();
}
