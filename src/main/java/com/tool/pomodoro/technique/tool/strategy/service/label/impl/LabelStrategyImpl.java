package com.tool.pomodoro.technique.tool.strategy.service.label.impl;

import com.tool.pomodoro.technique.tool.strategy.database.label.LabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.label.po.Label;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;
import com.tool.pomodoro.technique.tool.strategy.wrapper.label.LabelStrategyWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LabelStrategyImpl implements LabelStrategy {

    private final LabelDatabase labelDatabase;

    public LabelStrategyImpl(LabelDatabase labelDatabase) {
        this.labelDatabase = labelDatabase;
    }

    @Override
    public String add(String labelName) {
        return Optional.ofNullable(labelName)
                .filter(Predicate.not(String::isBlank))
                .flatMap(labelDatabase::selectByName)
                .map(Label::getLabelId)
                .orElseGet(() -> doAdd(labelName));
    }

    private String doAdd(String labelName) {
        String id = IdUtil.generate();

        var labelData = new Label();
        labelData.setLabelId(id);
        labelData.setLabelName(labelName);
        labelDatabase.save(labelData);

        return id;
    }

    @Override
    public void delete(String labelId) {
        labelDatabase.delete(labelId);
    }

    @Override
    public void update(LabelUpdateDto updateDto) {
        var label = LabelStrategyWrapper.wrapLabel(updateDto);
        labelDatabase.update(label);
    }

    @Override
    public Optional<LabelDto> get(String labelId) {
        return Optional.ofNullable(labelId)
                .filter(Predicate.not(String::isBlank))
                .flatMap(labelDatabase::selectById)
                .map(LabelStrategyWrapper::wrapLabelDto);
    }

    @Override
    public Optional<List<LabelDto>> list(List<String> labelIds) {
        return Optional.ofNullable(labelIds)
                .filter(Predicate.not(Collection::isEmpty))
                .flatMap(labelDatabase::selectByIds)
                .filter(Predicate.not(Collection::isEmpty))
                .map(LabelStrategyWrapper::wrapLabelDtoList);
    }

    @Override
    public Optional<List<LabelDto>> all() {
        return labelDatabase.selectAll()
                .filter(Predicate.not(Collection::isEmpty))
                .map(LabelStrategyWrapper::wrapLabelDtoList);
    }

    @Override
    public Optional<List<LabelDto>> fuzzyQueryByName(String labelName) {
        return labelDatabase.selectAll()
                .filter(Predicate.not(Collection::isEmpty))
                .map(labels -> labels.stream()
                        .filter(label -> label.getLabelName().contains(labelName))
                        .map(LabelStrategyWrapper::wrapLabelDto)
                        .collect(Collectors.toList()));
    }
}
