package com.oops.payroll.service;

import java.time.LocalDate;
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

                Attendance attendance = new Attendance();
                attendance.setEmployeeId(employeeId);
                attendance.setDate(LocalDate.now());
                attendance.setStatus(status);
                return attendanceRepository.save(attendance);
    }

    public Map<String, Object> getAttendanceSummary(int employeeId) {
                List<Attendance> employeeAttendance = attendanceRepository.findAllByEmployeeId(employeeId);

        long present = employeeAttendance.stream()
                .filter(record -> "PRESENT".equals(record.getStatus()))
                .count();

        long absent = employeeAttendance.stream()
                .filter(record -> "ABSENT".equals(record.getStatus()))
                .count();

        int total = (int) (present + absent);
        double percentage = total == 0 ? 0 : (present * 100.0) / total;

        return Map.of(
                "present", present,
                "absent", absent,
                "percentage", percentage
        );
    }

    public List<Attendance> getAllAttendance() {
                return attendanceRepository.findAll();
    }
}
