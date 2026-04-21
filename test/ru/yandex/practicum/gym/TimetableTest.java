package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Set<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertTrue(mondaySessions.contains(singleTrainingSession));

        //Проверить, что за вторник не вернулось занятий
        Set<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Set<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertTrue(mondaySessions.contains(mondayChildTrainingSession));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Set<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());

        TrainingSession[] sessions = thursdaySessions.toArray(new TrainingSession[0]);
        Assertions.assertEquals(new TimeOfDay(13, 0), sessions[0].getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), sessions[1].getTimeOfDay());

        // Проверить, что за вторник не вернулось занятий
        Set<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Set<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Set<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeWithMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group groupYoga = new Group("Йога", Age.ADULT, 60);
        Group groupPilates = new Group("Пилатес", Age.ADULT, 45);
        Group groupAerobics = new Group("Аэробика", Age.ADULT, 50);

        TimeOfDay time1000 = new TimeOfDay(10, 0);
        TimeOfDay time1200 = new TimeOfDay(12, 0);

        // Добавляем тренировки на понедельник в 10:00
        TrainingSession yoga = new TrainingSession(groupYoga, coach, DayOfWeek.MONDAY, time1000);
        TrainingSession pilates = new TrainingSession(groupPilates, coach, DayOfWeek.MONDAY, time1000);

        // Добавляем тренировку на понедельник в 12:00
        TrainingSession aerobics = new TrainingSession(groupAerobics, coach, DayOfWeek.MONDAY, time1200);

        // Добавляем тренировку на вторник в 10:00
        TrainingSession yogaTuesday = new TrainingSession(groupYoga, coach, DayOfWeek.TUESDAY, time1000);

        timetable.addNewTrainingSession(yoga);
        timetable.addNewTrainingSession(pilates);
        timetable.addNewTrainingSession(aerobics);
        timetable.addNewTrainingSession(yogaTuesday);

        // Проверяем: на понедельник в 10:00 должно быть 2 тренировки
        Set<TrainingSession> monday10am = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time1000);
        Assertions.assertEquals(2, monday10am.size());
        Assertions.assertTrue(monday10am.contains(yoga));
        Assertions.assertTrue(monday10am.contains(pilates));

        // Проверяем: на понедельник в 12:00 должна быть 1 тренировка
        Set<TrainingSession> monday12pm = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time1200);
        Assertions.assertEquals(1, monday12pm.size());
        Assertions.assertTrue(monday12pm.contains(aerobics));

        // Проверяем: на понедельник в другое время (14:00) нет тренировок
        TimeOfDay time1400 = new TimeOfDay(14, 0);
        Set<TrainingSession> monday2pm = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time1400);
        Assertions.assertTrue(monday2pm.isEmpty());

        // Проверяем: на вторник в 10:00 должна быть 1 тренировка
        Set<TrainingSession> tuesday10am = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.TUESDAY, time1000);
        Assertions.assertEquals(1, tuesday10am.size());
        Assertions.assertTrue(tuesday10am.contains(yogaTuesday));
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Алексей", "Иванович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 45);

        TimeOfDay time10 = new TimeOfDay(10, 0);

        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY, time10);
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.MONDAY, time10);

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что в одно время хранятся обе тренировки
        Set<TrainingSession> sessionsAtTime = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time10);
        Assertions.assertEquals(2, sessionsAtTime.size());

        // Проверяем, что за день тоже обе тренировки
        Set<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(2, mondaySessions.size());
    }

    @Test
    void testGetCountByCoachesSortedByCount() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Сергеевич");  // 2 тренировки
        Coach coach2 = new Coach("Танцулькина", "Мария", "Петровна"); // 5 тренировок
        Coach coach3 = new Coach("Петров", "Петр", "Николаевич"); // 1 тренировка
        Coach coach4 = new Coach("Григорьева", "Елена", "Александровна"); // 3 тренировки

        Group group = new Group("Танцы", Age.CHILD, 45);

        // coach3 - 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));

        // coach1 - 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        // coach4 - 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach4, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach4, DayOfWeek.FRIDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach4, DayOfWeek.SUNDAY, new TimeOfDay(11, 0)));

        // coach2 - 5 тренировок
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.MONDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(14, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        // Проверяем, что результат отсортирован по возрастанию количества тренировок
        List<Integer> counts = new ArrayList<>(result.values());
        Assertions.assertEquals(1, counts.get(0)); // coach3 - 1 тренировка
        Assertions.assertEquals(2, counts.get(1)); // coach1 - 2 тренировки
        Assertions.assertEquals(3, counts.get(2)); // coach4 - 3 тренировки
        Assertions.assertEquals(5, counts.get(3)); // coach2 - 5 тренировок

        // Проверяем, что порядок тренеров соответствует отсортированным значениям
        List<Coach> coaches = new ArrayList<>(result.keySet());
        Assertions.assertEquals(coach3, coaches.get(0));
        Assertions.assertEquals(coach1, coaches.get(1));
        Assertions.assertEquals(coach4, coaches.get(2));
        Assertions.assertEquals(coach2, coaches.get(3));
    }

    @Test
    void testGetCountByCoachesWithSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Прыжки на батуте", Age.CHILD, 60);

        TrainingSession session1 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0));
        TrainingSession session3 = new TrainingSession(group, coach, DayOfWeek.FRIDAY, new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        // Проверяем, что метод вернул одну запись (только для одного тренера)
        Assertions.assertEquals(1, result.size());

        // Проверяем, что количество тренировок для тренера Васильев равно 3
        Assertions.assertEquals(3, result.get(coach));
    }
}
