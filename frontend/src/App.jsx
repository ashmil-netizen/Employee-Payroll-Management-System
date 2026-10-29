import { useEffect, useRef, useState } from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Login from './pages/Login';
import EmployeeDashboard from './pages/EmployeeDashboard';
import EmployeeAttendance from './pages/EmployeeAttendance';
import LeaveManagement from './pages/LeaveManagement';
import SalaryDetails from './pages/SalaryDetails';
import AdminDashboard from './pages/AdminDashboard';

function ThemeToggle() {
  const toggleRef = useRef(null);
  const [isLightMode, setIsLightMode] = useState(
    () => window.localStorage.getItem('payrollTheme') === 'light'
  );

  useEffect(() => {
    const theme = isLightMode ? 'light' : 'dark';
    document.documentElement.dataset.theme = theme;
    window.localStorage.setItem('payrollTheme', theme);
  }, [isLightMode]);

  const toggleTheme = () => {
    const nextIsLightMode = !isLightMode;
    const applyTheme = () => {
      const theme = nextIsLightMode ? 'light' : 'dark';
      document.documentElement.dataset.theme = theme;
      window.localStorage.setItem('payrollTheme', theme);
      setIsLightMode(nextIsLightMode);
    };
    const toggleBounds = toggleRef.current?.getBoundingClientRect();

    if (toggleBounds && document.startViewTransition
      && !window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      document.documentElement.style.setProperty(
        '--theme-origin-x',
        `${toggleBounds.left + toggleBounds.width / 2}px`
      );
      document.documentElement.style.setProperty(
        '--theme-origin-y',
        `${toggleBounds.top + toggleBounds.height / 2}px`
      );
      document.startViewTransition(applyTheme);
      return;
    }

    applyTheme();
  };

  return (
    <button
      ref={toggleRef}
      type="button"
      className="theme-toggle"
      onClick={toggleTheme}
      aria-label={`Switch to ${isLightMode ? 'dark' : 'light'} mode`}
      title={`Switch to ${isLightMode ? 'dark' : 'light'} mode`}
    >
      {isLightMode ? (
        <svg aria-hidden="true" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <path strokeLinecap="round" strokeLinejoin="round" d="M20.2 15.3A8.5 8.5 0 0 1 8.7 3.8 8.5 8.5 0 1 0 20.2 15.3Z" />
        </svg>
      ) : (
        <svg aria-hidden="true" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
          <circle cx="12" cy="12" r="4" />
          <path strokeLinecap="round" d="M12 2v2m0 16v2M4.93 4.93l1.42 1.42m11.3 11.3 1.42 1.42M2 12h2m16 0h2M4.93 19.07l1.42-1.42m11.3-11.3 1.42-1.42" />
        </svg>
      )}
      <span>{isLightMode ? 'Dark mode' : 'Light mode'}</span>
    </button>
  );
}

function App() {
  return (
    <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <ThemeToggle />
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/employee" element={<EmployeeDashboard />} />
        <Route path="/attendance" element={<EmployeeAttendance />} />
        <Route path="/leave-management" element={<LeaveManagement />} />
        <Route path="/salary" element={<SalaryDetails />} />
        <Route path="/admin" element={<AdminDashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
