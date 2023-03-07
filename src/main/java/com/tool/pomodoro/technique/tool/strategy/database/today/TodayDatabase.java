package com.tool.pomodoro.technique.tool.strategy.database.today;

import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;

import java.util.List;
import java.util.Optional;

public interface TodayDatabase {
    void save(Today today);

    void saveBatch(List<Today> today);

    void deleteById(String uuid);

    void update(Today today);

    Optional<Today> selectById(String uuid);

    Optional<List<Today>> selectAll();
}
