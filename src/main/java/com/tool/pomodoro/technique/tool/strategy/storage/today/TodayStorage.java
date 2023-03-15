package com.tool.pomodoro.technique.tool.strategy.storage.today;

import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.util.List;
import java.util.Optional;

public interface TodayStorage {
    void save(Today today);

    void saveBatch(List<Today> today);

    void deleteById(String uuid);

    void update(Today today);

    Optional<Today> selectById(String uuid);

    Optional<List<Today>> selectAll();
}
