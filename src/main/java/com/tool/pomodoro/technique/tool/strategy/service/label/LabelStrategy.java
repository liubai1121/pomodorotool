package com.tool.pomodoro.technique.tool.strategy.service.label;

import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;

import java.util.List;
import java.util.Optional;

public interface LabelStrategy {

    String add(String labelName);

    void delete(String labelId);

    void update(LabelUpdateDto updateDto);

    Optional<LabelDto> get(String labelId);

    Optional<List<LabelDto>> list(List<String> labelIds);

    Optional<List<LabelDto>> all();

    Optional<List<LabelDto>> fuzzyQueryByName(String labelName);
}
