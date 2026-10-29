import { useEffect, useState } from 'react';
import { getEmployees, getLeaveRequests, updateLeaveRequest } from '../services/api';

function LeaveManagement() {
  const [requests, setRequests] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([getLeaveRequests(), getEmployees()])
      .then(([leaveData, employeeData]) => {
        setRequests(leaveData);
        setEmployees(employeeData);
      })
      .catch((requestError) => setError(requestError.message));
  }, []);

  const decideRequest = async (leaveId, decision) => {
    try {
      const updatedRequest = await updateLeaveRequest(leaveId, decision);
      setRequests((previous) => previous.map((request) => (
        request.leaveId === leaveId ? updatedRequest : request
      )));
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
        <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Admin</p>
        <h1 className="mt-2 text-3xl font-bold text-slate-900">Leave Management</h1>
        {error && <p role="alert" className="mt-4 text-sm text-red-700">{error}</p>}

        <div className="mt-6 overflow-x-auto">
          <table className="min-w-full border border-slate-200 text-left text-sm">
            <thead className="bg-slate-100 text-slate-700">
              <tr>
                <th className="px-4 py-3">Employee</th>
                <th className="px-4 py-3">Leave Type</th>
                <th className="px-4 py-3">Dates</th>
                <th className="px-4 py-3">Reason</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((request) => (
                <tr key={request.leaveId} className="border-t border-slate-200">
                  <td className="px-4 py-3">
                    {employees.find((employee) => employee.employeeId === request.employeeId)?.name || request.employeeId}
                  </td>
                  <td className="px-4 py-3">{request.leaveType}</td>
                  <td className="px-4 py-3">{request.fromDate} to {request.toDate}</td>
                  <td className="px-4 py-3">{request.reason}</td>
                  <td className="px-4 py-3">
                    <span className={`rounded-full px-2 py-1 text-xs font-semibold ${request.status === 'PENDING' ? 'bg-amber-100 text-amber-700' : 'bg-slate-100 text-slate-700'}`}>
                      {request.status}
                    </span>
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex gap-2">
                      {request.status === 'PENDING' && (
                        <>
                          <button onClick={() => decideRequest(request.leaveId, 'approve')} className="rounded-lg bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white">Approve</button>
                          <button onClick={() => decideRequest(request.leaveId, 'reject')} className="rounded-lg bg-red-500 px-3 py-1.5 text-xs font-semibold text-white">Reject</button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
              {requests.length === 0 && (
                <tr className="border-t border-slate-200">
                  <td colSpan="6" className="px-4 py-6 text-center text-slate-500">No leave requests.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default LeaveManagement;
