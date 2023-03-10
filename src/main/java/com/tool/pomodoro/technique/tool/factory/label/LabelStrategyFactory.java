package com.tool.pomodoro.technique.tool.factory.label;

import com.tool.pomodoro.technique.tool.database.file.label.FileLabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.label.LabelDatabase;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.impl.LabelStrategyImpl;

public class LabelStrategyFactory {

    private final static LabelDatabase database = new FileLabelDatabase();
    private final static LabelStrategy labelStrategy = new LabelStrategyImpl(database);

    public static LabelStrategy create() {
        return labelStrategy;
    }
}
