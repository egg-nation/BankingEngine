package com.mambujava1.server.scheduler;

import java.time.LocalDate;

public class TimeSimulation {

    public static LocalDate currentDate = LocalDate.now();

    public static void addDays(int nrDays) {

        while (nrDays > 0) {
            nrDays--;
            currentDate = currentDate.plusDays(1);
            Scheduler.getInstance().performDueDateAccountsUpdate();
        }
    }
}
