package com.oops.payroll.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oops.payroll.model.Attendance;
import com.oops.payroll.service.AttendanceService;

/**
 * Exposes attendance endpoints for marking and viewing attendance.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance/{employeeId}")
    public ResponseEntity<Map<String, Object>> getAttendance(@PathVariable int employeeId) {
        return ResponseEntity.ok(attendanceService.getAttendanceSummary(employeeId));
    }

    @GetMapping("/attendance")
    public ResponseEntity<java.util.List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendance(@RequestParam int employeeId,
                                                    @RequestParam String status) {
        return ResponseEntity.ok(attendanceService.markAttendance(employeeId, status));
    }
}
