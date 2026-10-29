import { useEffect, useState } from 'react';
import { getPayrollByEmployeeId } from '../services/api';

function SalaryDetails() {
  const employee = JSON.parse(localStorage.getItem('payrollUser') || '{}');
  const [salary, setSalary] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!employee.employeeId) return;
    getPayrollByEmployeeId(employee.employeeId)
      .then(setSalary)
      .catch((requestError) => setError(requestError.message));
  }, [employee.employeeId]);

  if (error) return <main className="min-h-screen p-6 text-red-700">{error}</main>;
  if (!salary) return <main className="min-h-screen p-6 text-slate-600">Loading salary details...</main>;

  return (
    <div className="min-h-screen bg-slate-100 p-6">
      <div className="mx-auto max-w-5xl rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
        <p className="text-sm font-semibold uppercase tracking-[0.2em] text-indigo-600">Salary</p>
        <h1 className="mt-2 text-3xl font-bold text-slate-900">Salary Details</h1>

        <div className="mt-8 grid gap-6 md:grid-cols-2">
          <div className="space-y-4 rounded-2xl bg-slate-50 p-5 ring-1 ring-slate-200">
            <div className="flex justify-between text-slate-700">
              <span>Basic Salary</span>
              <span>₹{salary.basicSalary.toLocaleString()}</span>
            </div>
            <div className="flex justify-between text-slate-700">
              <span>Allowances</span>
              <span>₹{salary.allowances.toLocaleString()}</span>
            </div>
            <div className="flex justify-between text-slate-700">
              <span>Overtime</span>
              <span>₹{salary.overtime.toLocaleString()}</span>
            </div>
            <div className="flex justify-between text-slate-700">
              <span>Bonus</span>
              <span>₹{salary.bonus.toLocaleString()}</span>
            </div>
            <div className="flex justify-between text-slate-700">
              <span>Deductions</span>
              <span>₹{salary.deductions.toLocaleString()}</span>
            </div>

            <div className="my-4 h-px bg-slate-300" />

            <div className="flex justify-between text-xl font-bold text-slate-900">
              <span>NET SALARY</span>
              <span>₹{salary.netSalary.toLocaleString()}</span>
            </div>
          </div>

          <div className="rounded-2xl bg-slate-900 p-5 text-white shadow-sm">
            <h2 className="text-xl font-bold">Salary Slip</h2>
            <div className="mt-6 space-y-3 text-sm">
              <p><span className="text-slate-300">Employee ID:</span> {salary.employeeId}</p>
              <p><span className="text-slate-300">Employee Name:</span> {salary.employeeName}</p>
              <p><span className="text-slate-300">Department:</span> {salary.department}</p>
              <p><span className="text-slate-300">Designation:</span> {salary.designation}</p>
              <p><span className="text-slate-300">Pay Period:</span> {salary.month}</p>
            </div>

            <div className="mt-6 space-y-3 text-sm">
              <p className="font-semibold text-slate-200">Earnings</p>
              <p>Basic Salary: ₹{salary.basicSalary.toLocaleString()}</p>
              <p>Allowances: ₹{salary.allowances.toLocaleString()}</p>
              <p>Overtime: ₹{salary.overtime.toLocaleString()}</p>
              <p>Bonus: ₹{salary.bonus.toLocaleString()}</p>
            </div>

            <div className="mt-6 space-y-3 text-sm">
              <p className="font-semibold text-slate-200">Deductions</p>
              <p>Deductions: ₹{salary.deductions.toLocaleString()}</p>
            </div>

            <div className="mt-6 border-t border-slate-700 pt-4 text-lg font-bold">
              NET SALARY: ₹{salary.netSalary.toLocaleString()}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SalaryDetails;
