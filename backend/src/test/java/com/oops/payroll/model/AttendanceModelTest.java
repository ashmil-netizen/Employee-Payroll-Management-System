package com.oops.payroll.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class AttendanceModelTest {

    @Test
    void acceptsAvailableStatuses() {
        Attendance attendance = new Attendance(1, 1001, LocalDate.now(), " available ");

        assertEquals("AVAILABLE", attendance.getStatus());

        attendance.setStatus("unavailable");
        assertEquals("UNAVAILABLE", attendance.getStatus());
    }

    @Test
    void rejectsUnknownStatuses() {
        Attendance attendance = new Attendance();

        assertThrows(IllegalArgumentException.class, () -> attendance.setStatus("UNKNOWN"));
    }
}
