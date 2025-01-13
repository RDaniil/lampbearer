package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.interfaces.Schedulable;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class Scheduler {
    private static final TreeMap<Integer, List<Schedulable>> timeToSchedulablesMap =
            new TreeMap<>();
    public static int currentTime = 0;


    public static void add(Schedulable schedulable) {
        int time = currentTime + schedulable.getTime();
        List<Schedulable> schedulables = timeToSchedulablesMap
                .putIfAbsent(time, new ArrayList<>() {{
                    add(schedulable);
                }});
        if (schedulables != null) schedulables.add(schedulable);
    }


    public static void remove(Schedulable schedulable) {
        List<Integer> emptySchedules = new ArrayList<>();
        for (var timeToSchedulables : timeToSchedulablesMap.entrySet()) {
            List<Schedulable> schedulables = timeToSchedulables.getValue();
            schedulables.remove(schedulable);

            if (schedulables.isEmpty()) {
                emptySchedules.add(timeToSchedulables.getKey());
            }
        }

        for (Integer emptyScheduleKey : emptySchedules) {
            timeToSchedulablesMap.remove(emptyScheduleKey);
        }
    }


    /**
     * Returns the first Schedulable
     */
    public static Schedulable peek() {
        currentTime = timeToSchedulablesMap.firstKey();
        List<Schedulable> scheduledToCurrentTime = timeToSchedulablesMap.get(currentTime);

        /*Сущности с скоростью 0 - выполняются "мгновенно" вне очереди*/
        var immediateSchedulable = scheduledToCurrentTime.stream()
                .filter(s -> s.getTime() == 0).findFirst();
        if (immediateSchedulable.isPresent()) {
            return immediateSchedulable.get();
        }

        var schedulable = scheduledToCurrentTime.get(0);
        if (schedulable != null) return schedulable;

        throw new RuntimeException("Расписание сломалось, " + currentTime);
    }


    /**
     * Returns and removes first Schedulable
     */
    public static Schedulable poll() {
        var schedulable = peek();
        remove(schedulable);
        return schedulable;
    }
}
