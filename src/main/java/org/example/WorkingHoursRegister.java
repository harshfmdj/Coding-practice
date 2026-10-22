package org.example;

import java.util.*;

public class WorkingHoursRegister {

    // Inner class representing a completed work session
    public static class Session {
        long start;
        long end;
        String position;
        double salary;
        long duration;
        boolean isDouble;

        public Session(long start, long end, String position, double salary, boolean isDouble) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.salary = salary;
            this.duration = end - start;
            this.isDouble = isDouble;
        }
    }

    // Inner class representing an Employee's state
    public static class Employee {
        String empId;
        String position;
        double salary;
        String pendingPosition;
        Double pendingSalary;
        Long checkedInAt; // null if not checked in, otherwise contains the timestamp
        List<Session> sessions;

        public Employee(String empId, String position, double salary) {
            this.empId = empId;
            this.position = position;
            this.salary = salary;
            this.pendingPosition = null;
            this.pendingSalary = null;
            this.checkedInAt = null;
            this.sessions = new ArrayList<>();
        }
    }

    private Map<String, Employee> employees;
    private List<long[]> grantPeriods; // Stores [start, end] pairs
    private double totalDoublePayIssued;

    public WorkingHoursRegister() {
        this.employees = new HashMap<>();
        this.grantPeriods = new ArrayList<>();
        this.totalDoublePayIssued = 0.0;
    }

    // -------------------------------------------------------------
    // Level 1: Basic CRUD & Swiping
    // -------------------------------------------------------------
    public void addEmployee(String empId, String initialPosition, double initialSalary) {
        employees.putIfAbsent(empId, new Employee(empId, initialPosition, initialSalary));
    }

    public void swipe(String empId, long timestamp) {
        Employee emp = employees.get(empId);
        if (emp == null) return;

        if (emp.checkedInAt == null) {
            // --- SWIPE IN ---
            // Level 3: Apply pending promotion right before swiping in
            if (emp.pendingPosition != null) {
                emp.position = emp.pendingPosition;
                emp.salary = emp.pendingSalary;
                emp.pendingPosition = null;
                emp.pendingSalary = null;
            }
            emp.checkedInAt = timestamp;
        } else {
            // --- SWIPE OUT ---
            long startTime = emp.checkedInAt;
            long endTime = timestamp;
            emp.checkedInAt = null;

            // Check Level 4: Double pay condition
            boolean isDouble = fallsCompletelyInGrant(startTime, endTime);
            long duration = endTime - startTime;
            double baseEarnings = duration * emp.salary;

            if (isDouble) {
                totalDoublePayIssued += baseEarnings;
            }

            // Record session with historical position and salary
            Session session = new Session(startTime, endTime, emp.position, emp.salary, isDouble);
            emp.sessions.add(session);
        }
    }

    public long getTotalWorkingHours(String empId) {
        Employee emp = employees.get(empId);
        if (emp == null) return 0;

        long totalHours = 0;
        for (Session s : emp.sessions) {
            totalHours += s.duration;
        }
        return totalHours;
    }

    // -------------------------------------------------------------
    // Level 2: Top N Employees by Working Hours
    // -------------------------------------------------------------
    public List<String> getTopNEmployees(int n) {
        List<Map.Entry<String, Long>> empHoursList = new ArrayList<>();

        for (String empId : employees.keySet()) {
            empHoursList.add(new AbstractMap.SimpleEntry<>(empId, getTotalWorkingHours(empId)));
        }

        // Sort by total hours descending, and alphabetically by empId for stability
        empHoursList.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp != 0) return cmp;
            return a.getKey().compareTo(b.getKey());
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, empHoursList.size()); i++) {
            result.add(empHoursList.get(i).getKey());
        }
        return result;
    }

    // -------------------------------------------------------------
    // Level 3: Delayed Promotion & Historical Salary
    // -------------------------------------------------------------
    public void requestPromotion(String empId, String newPosition, double newSalary) {
        Employee emp = employees.get(empId);
        if (emp != null) {
            emp.pendingPosition = newPosition;
            emp.pendingSalary = newSalary;
        }
    }

    public List<Session> getEmployeeSalaryHistory(String empId) {
        Employee emp = employees.get(empId);
        if (emp == null) return new ArrayList<>();
        return emp.sessions;
    }

    // -------------------------------------------------------------
    // Level 4: Double Pay during Grant Period
    // -------------------------------------------------------------
    public void addGrantPeriod(long start, long end) {
        grantPeriods.add(new long[]{start, end});
    }

    private boolean fallsCompletelyInGrant(long start, long end) {
        for (long[] period : grantPeriods) {
            long gStart = period[0];
            long gEnd = period[1];
            if (gStart <= start && end <= gEnd) {
                return true;
            }
        }
        return false;
    }

    public double getTotalDoublePay() {
        return totalDoublePayIssued;
    }
}