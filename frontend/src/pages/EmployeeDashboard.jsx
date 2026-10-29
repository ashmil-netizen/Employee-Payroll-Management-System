import { Link, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { getAttendanceByEmployeeId, markAttendance } from '../services/api';

function EmployeeDashboard() {
  const location = useLocation();
  const employee = location.state?.employee || JSON.parse(localStorage.getItem('payrollUser') || 'null') || {
    name: 'Employee',
    employeeId: 'N/A'
  };
  const [attendance, setAttendance] = useState({ available: 0, unavailable: 0, percentage: 0 });
  const [error, setError] = useState('');
  const [savingStatus, setSavingStatus] = useState(false);

  useEffect(() => {
    if (!employee.employeeId || employee.employeeId === 'N/A') return;
    getAttendanceByEmployeeId(employee.employeeId)
      .then(setAttendance)
      .catch((requestError) => setError(requestError.message));
  }, [employee.employeeId]);

  const recordAttendance = async (status) => {
    setSavingStatus(true);
    try {
      await markAttendance(employee.employeeId, status);
      setAttendance(await getAttendanceByEmployeeId(employee.employeeId));
      setError('');
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSavingStatus(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-6xl">
        <div className="mb-6 rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Dashboard</p>
          <h1 className="mt-2 text-3xl font-bold text-slate-900">Good Morning, {employee.name}</h1>
          <p className="mt-2 text-sm text-slate-500">Your workspace <span className="mx-2 text-indigo-400">/</span> Availability, attendance, and pay</p>
        </div>

        <div className="grid gap-6 md:grid-cols-3">
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Employee ID</p>
            <p className="mt-2 text-2xl font-bold text-slate-900">{employee.employeeId || 'N/A'}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Employee Name</p>
            <p className="mt-2 text-2xl font-bold text-slate-900">{employee.name}</p>
          </div>
          <div className="rounded-2xl bg-white p-5 shadow-sm ring-1 ring-slate-200">
            <p className="text-sm text-slate-500">Current Availability</p>
            <div className="mt-3 flex gap-3">
              <button
                onClick={() => recordAttendance('AVAILABLE')}
                disabled={savingStatus || attendance.currentStatus === 'AVAILABLE'}
                aria-pressed={attendance.currentStatus === 'AVAILABLE'}
                className={`rounded-lg bg-emerald-600 px-3 py-2 text-sm font-semibold text-white transition-opacity disabled:cursor-not-allowed ${attendance.currentStatus === 'AVAILABLE' ? 'opacity-40' : 'hover:opacity-90'}`}
              >
                Available
              </button>
              <button
                onClick={() => recordAttendance('UNAVAILABLE')}
                disabled={savingStatus || attendance.currentStatus === 'UNAVAILABLE'}
                aria-pressed={attendance.currentStatus === 'UNAVAILABLE'}
                className={`rounded-lg bg-red-500 px-3 py-2 text-sm font-semibold text-white transition-opacity disabled:cursor-not-allowed ${attendance.currentStatus === 'UNAVAILABLE' ? 'opacity-40' : 'hover:opacity-90'}`}
              >
                Unavailable
              </button>
            </div>
            <p className="mt-3 text-sm text-slate-600">{Number(attendance.percentage).toFixed(1)}% availability</p>
            {error && <p role="alert" className="mt-2 text-sm text-red-700">{error}</p>}
          </div>
        </div>

        <div className="mt-6 grid gap-6 md:grid-cols-2">
          <Link to="/attendance" className="rounded-2xl bg-indigo-600 p-6 text-white shadow-sm transition hover:bg-indigo-700">
            <p className="text-sm uppercase tracking-[0.2em] text-indigo-100">Employee</p>
            <h2 className="mt-2 text-2xl font-bold">Attendance & Leave</h2>
          </Link>

          <Link to="/salary" className="rounded-2xl bg-emerald-600 p-6 text-white shadow-sm transition hover:bg-emerald-700">
            <p className="text-sm uppercase tracking-[0.2em] text-emerald-100">Payroll</p>
            <h2 className="mt-2 text-2xl font-bold">Salary Slip</h2>
          </Link>

        </div>
      </div>
    </div>
  );
}

export default EmployeeDashboard;
