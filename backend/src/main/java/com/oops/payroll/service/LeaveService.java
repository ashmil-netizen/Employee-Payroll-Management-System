package com.oops.payroll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oops.payroll.model.LeaveRequest;
import com.oops.payroll.repository.EmployeeRepository;
import com.oops.payroll.repository.LeaveRequestRepository;

/**
 * Manages submitted leave requests and their approval status.
 */
@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public LeaveRequest submitLeaveRequest(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            throw new IllegalArgumentException("Leave request cannot be null.");
        }

        leaveRequest.setStatus("PENDING");
        if (!employeeRepository.existsById(leaveRequest.getEmployeeId())) {
            throw new IllegalArgumentException("Employee not found.");
        }
        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getRequestsForEmployee(int employeeId) {
        return leaveRequestRepository.findAllByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getAllRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest approveRequest(int leaveId) {
        LeaveRequest request = findLeaveRequest(leaveId);
        request.setStatus("APPROVED");
        return leaveRequestRepository.save(request);
    }

    public LeaveRequest rejectRequest(int leaveId) {
        LeaveRequest request = findLeaveRequest(leaveId);
        request.setStatus("REJECTED");
        return leaveRequestRepository.save(request);
    }

    private LeaveRequest findLeaveRequest(int leaveId) {
        return leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found."));
    }
}
