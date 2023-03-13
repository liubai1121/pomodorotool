package com.tool.pomodoro.technique.tool.strategy.wrapper.label;

import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;

import java.util.List;
import java.util.stream.Collectors;

public class LabelStrategyWrapper {
    private LabelStrategyWrapper() {
    }


    public static LabelDto wrapLabelDto(Label label) {
        var labelDto = new LabelDto();
        labelDto.setLabelId(label.getLabelId());
        labelDto.setLabelName(label.getLabelName());
        return labelDto;
    }

    public static List<LabelDto> wrapLabelDtoList(List<Label> labels) {
        return labels.stream()
                .map(LabelStrategyWrapper::wrapLabelDto)
                .collect(Collectors.toList());
    }

    public static Label wrapLabel(LabelUpdateDto updateDto) {
        var label = new Label();
        label.setLabelId(updateDto.getLabelId());
        label.setLabelName(updateDto.getLabelName());
        return label;
    }
}
