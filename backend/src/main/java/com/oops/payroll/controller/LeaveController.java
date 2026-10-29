package com.oops.payroll.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oops.payroll.model.LeaveRequest;
import com.oops.payroll.service.LeaveService;

/**
 * Exposes leave request endpoints for employees and admins.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/leave")
    public ResponseEntity<LeaveRequest> submitLeave(@RequestBody LeaveRequest leaveRequest) {
        return ResponseEntity.ok(leaveService.submitLeaveRequest(leaveRequest));
    }

    @GetMapping("/leave/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaves(@PathVariable int employeeId) {
        return ResponseEntity.ok(leaveService.getRequestsForEmployee(employeeId));
    }

    @GetMapping("/leave")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllRequests());
    }

    @PostMapping("/leave/approve/{leaveId}")
    public ResponseEntity<LeaveRequest> approveLeave(@PathVariable int leaveId) {
        return ResponseEntity.ok(leaveService.approveRequest(leaveId));
    }

    @PostMapping("/leave/reject/{leaveId}")
    public ResponseEntity<LeaveRequest> rejectLeave(@PathVariable int leaveId) {
        return ResponseEntity.ok(leaveService.rejectRequest(leaveId));
    }
}
