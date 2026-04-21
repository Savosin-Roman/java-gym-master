package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // Map для конкретного дня
        Map<TimeOfDay, Set<TrainingSession>> sessionsByTime = timetable.get(day);
        if (sessionsByTime == null) {
            sessionsByTime = new TreeMap<>(TimeOfDay::compareTo);
            timetable.put(day, sessionsByTime);
        }

        // Сет для конкретного времени
        Set<TrainingSession> sessions = sessionsByTime.get(time);
        if (sessions == null) {
            sessions = new TreeSet<>(
                    Comparator.comparing(TrainingSession::getTimeOfDay)
                            .thenComparing(s -> s.getGroup().getTitle())
                            .thenComparing(s -> s.getCoach().getSurname())
            );
            sessionsByTime.put(time, sessions);
        }

        // сохраняем занятие
        sessions.add(trainingSession);
    }

    // все тренировки за день
    public Set<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, Set<TrainingSession>> sessionsByTime = timetable.get(dayOfWeek);
        if (sessionsByTime == null || sessionsByTime.isEmpty()) {
            return Collections.emptySet();
        }

        Set<TrainingSession> result = new HashSet<>();
        for (Set<TrainingSession> sessions : sessionsByTime.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    // тренировки за конкретное время
    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, Set<TrainingSession>> sessionsByTime = timetable.get(dayOfWeek);
        if (sessionsByTime == null) {
            return Collections.emptySet();
        }
        return sessionsByTime.getOrDefault(timeOfDay, Collections.emptySet());
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> counterOfTrainings = new LinkedHashMap<>();

        for (Map<TimeOfDay, Set<TrainingSession>> sessions : timetable.values()) {
            for (Set<TrainingSession> session : sessions.values()) {
                for (TrainingSession t : session) {
                    if (counterOfTrainings.containsKey(t.getCoach())) {
                        Integer counter = counterOfTrainings.get(t.getCoach());
                        counterOfTrainings.put(t.getCoach(), counter + 1);
                    } else {
                        counterOfTrainings.put(t.getCoach(), 1);
                    }
                }
            }
        }
        return sortedBySession(counterOfTrainings);
    }

    public Map<Coach, Integer> sortedBySession(Map<Coach, Integer> sortCountByCoaches) {
        return sortCountByCoaches.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
