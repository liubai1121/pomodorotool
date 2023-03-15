package com.tool.pomodoro.technique.tool.strategy.wrapper.label;

import com.tool.pomodoro.technique.tool.strategy.storage.label.po.Label;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;

import java.util.List;
import java.util.stream.Collectors;

public class LabelStrategyWrapper {
    private LabelStrategyWrapper() {
    }


    public static LabelDto wrapLabelDto(Label label) {
        return new LabelDto(label.labelId(), label.labelName());
    }

    public static List<LabelDto> wrapLabelDtoList(List<Label> labels) {
        return labels.stream()
                .map(LabelStrategyWrapper::wrapLabelDto)
                .collect(Collectors.toList());
    }

    public static Label wrapLabel(LabelUpdateDto updateDto) {
        return new Label(updateDto.labelId(), updateDto.labelName());
    }
}
