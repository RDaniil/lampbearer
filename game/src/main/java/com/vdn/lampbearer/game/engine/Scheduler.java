package com.vdn.lampbearer.game.engine;

import com.vdn.lampbearer.entites.interfaces.Schedulable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
@Slf4j
public class Scheduler {
    private static final TreeMap<Integer, List<Schedulable>> timeToSchedulable = new TreeMap<>();
    private static int currentTime = 0;

    public static void add(Schedulable schedulable) {
        int schedulableNextTime = currentTime + schedulable.getTime();
        if (!timeToSchedulable.containsKey(schedulableNextTime)) {
            timeToSchedulable.putIfAbsent(schedulableNextTime, new ArrayList<>());
        }
        timeToSchedulable.get(schedulableNextTime).add(schedulable);
    }

    public static void remove(Schedulable schedulable) {
        List<Integer> emptySchedules = new ArrayList<>();
        for (var timeToSchedul : timeToSchedulable.entrySet()) {
            List<Schedulable> schedulables = timeToSchedul.getValue();
            schedulables.remove(schedulable);

            if (schedulables.isEmpty()) {
                emptySchedules.add(timeToSchedul.getKey());
            }
        }

        for (Integer emptyScheduleKey : emptySchedules) {
            timeToSchedulable.remove(emptyScheduleKey);
        }
    }

    public static Schedulable getNext() {
        currentTime = timeToSchedulable.firstKey();
        List<Schedulable> scheduledToCurrentTime = timeToSchedulable.get(currentTime);
        var schedulable = scheduledToCurrentTime.get(0);
        if (schedulable != null) {
            remove(schedulable);
            return schedulable;
        }
        throw new RuntimeException("Расписание сломалось, " + currentTime);
    }

}
