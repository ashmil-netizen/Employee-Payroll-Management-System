package com.oops.payroll.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.oops.payroll.model.Attendance;
import com.oops.payroll.repository.AttendanceRepository;
import com.oops.payroll.repository.EmployeeRepository;

/**
 * Handles attendance tracking and calculates simple attendance summary values.
 */
@Service
public class AttendanceService {

        private final AttendanceRepository attendanceRepository;
        private final EmployeeRepository employeeRepository;

        public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
                this.attendanceRepository = attendanceRepository;
                this.employeeRepository = employeeRepository;
        }

    public Attendance markAttendance(int employeeId, String status) {
                if (!employeeRepository.existsById(employeeId)) {
                        throw new IllegalArgumentException("Employee not found.");
                }

                LocalDate today = LocalDate.now();
                Attendance attendance = attendanceRepository
                                .findFirstByEmployeeIdAndDateOrderByAttendanceIdDesc(employeeId, today)
                                .orElseGet(Attendance::new);
                attendance.setEmployeeId(employeeId);
                attendance.setDate(today);
                attendance.setStatus(status);
                return attendanceRepository.save(attendance);
    }

    public Map<String, Object> getAttendanceSummary(int employeeId) {
                List<Attendance> employeeAttendance = attendanceRepository.findAllByEmployeeId(employeeId);

        Map<LocalDate, Attendance> latestByDate = new HashMap<>();
        for (Attendance record : employeeAttendance) {
                latestByDate.merge(record.getDate(), record,
                                (existing, candidate) -> candidate.getAttendanceId() > existing.getAttendanceId()
                                                ? candidate : existing);
        }

        long available = latestByDate.values().stream()
                .filter(record -> "AVAILABLE".equals(record.getStatus()))
                .count();

        long unavailable = latestByDate.values().stream()
                .filter(record -> "UNAVAILABLE".equals(record.getStatus()))
                .count();

        int total = (int) (available + unavailable);
        double percentage = total == 0 ? 0 : (available * 100.0) / total;
        Attendance todayRecord = latestByDate.get(LocalDate.now());
        String currentStatus = todayRecord == null ? "NOT_SET" : todayRecord.getStatus();

        return Map.of(
                "available", available,
                "unavailable", unavailable,
                "percentage", percentage,
                "currentStatus", currentStatus == null ? "NOT_SET" : currentStatus
        );
    }

    public List<Attendance> getAllAttendance() {
                return attendanceRepository.findAll();
    }
}
