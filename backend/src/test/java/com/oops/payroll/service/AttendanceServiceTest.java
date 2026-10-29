package com.oops.payroll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.oops.payroll.model.Attendance;
import com.oops.payroll.repository.AttendanceRepository;
import com.oops.payroll.repository.EmployeeRepository;

class AttendanceServiceTest {

    @Test
    void changingAvailabilityUpdatesTodaysRecord() {
        int employeeId = 1001;
        LocalDate today = LocalDate.now();
        Attendance todaysRecord = new Attendance(1, employeeId, today, "AVAILABLE");
        AttendanceRepository attendanceRepository = mock(AttendanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(attendanceRepository.findFirstByEmployeeIdAndDateOrderByAttendanceIdDesc(employeeId, today))
                .thenReturn(Optional.of(todaysRecord));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AttendanceService service = new AttendanceService(attendanceRepository, employeeRepository);

        Attendance updatedRecord = service.markAttendance(employeeId, "UNAVAILABLE");

        assertSame(todaysRecord, updatedRecord);
        assertEquals("UNAVAILABLE", updatedRecord.getStatus());
        verify(attendanceRepository).save(todaysRecord);
    }

    @Test
    void summaryUsesLatestStatusPerDayAndReturnsTodaysStatus() {
        int employeeId = 1001;
        LocalDate today = LocalDate.now();
        Attendance yesterday = new Attendance(1, employeeId, today.minusDays(1), "AVAILABLE");
        Attendance previousTodayStatus = new Attendance(2, employeeId, today, "AVAILABLE");
        Attendance currentTodayStatus = new Attendance(3, employeeId, today, "UNAVAILABLE");
        AttendanceRepository attendanceRepository = mock(AttendanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(attendanceRepository.findAllByEmployeeId(employeeId))
                .thenReturn(List.of(yesterday, previousTodayStatus, currentTodayStatus));
        AttendanceService service = new AttendanceService(attendanceRepository, employeeRepository);

        Map<String, Object> summary = service.getAttendanceSummary(employeeId);

        assertEquals(1L, summary.get("available"));
        assertEquals(1L, summary.get("unavailable"));
        assertEquals(50.0, summary.get("percentage"));
        assertEquals("UNAVAILABLE", summary.get("currentStatus"));
    }
}
