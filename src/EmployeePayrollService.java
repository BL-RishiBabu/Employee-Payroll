import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmployeePayrollService {
    private final List<EmployeePayrollData> employeeList;
    private final EmployeePayrollFileIOService fileIOService;

    public EmployeePayrollService() {
        this.employeeList = new ArrayList<>();
        this.fileIOService = new EmployeePayrollFileIOService();
    }

    public void addEmployeeFromConsole(Scanner scanner) {
        System.out.print("Enter employee id: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter employee salary: ");
        double salary = Double.parseDouble(scanner.nextLine().trim());
        EmployeePayrollData newEmployee = new EmployeePayrollData(id, name, salary);
        employeeList.add(newEmployee);
        System.out.println("Employee added: " + newEmployee);
    }

    public void writePayrollDataToFile() {
        try {
            fileIOService.writeData(employeeList);
            System.out.println("Payroll data written to file: " + fileIOService.getFilePath());
        } catch (IOException e) {
            System.err.println("Unable to write payroll data to file: " + e.getMessage());
        }
    }

    public void readPayrollDataFromFile() {
        try {
            List<EmployeePayrollData> employees = fileIOService.readData();
            if (employees.isEmpty()) {
                System.out.println("No payroll data found in file.");
                return;
            }
            System.out.println("Employee payroll data from file:");
            printFormatted(employees);
        } catch (IOException e) {
            System.err.println("Unable to read payroll data from file: " + e.getMessage());
        }
    }

    public void printPayrollData() {
        if (employeeList.isEmpty()) {
            System.out.println("No employee payroll data available in memory.");
            return;
        }
        System.out.println("Employee payroll data in memory:");
        printFormatted(employeeList);
    }

    public void printEntryCount() {
        try {
            long count = fileIOService.countEntries();
            System.out.println("Number of payroll entries in file: " + count);
        } catch (IOException e) {
            System.err.println("Unable to count payroll entries: " + e.getMessage());
        }
    }

    public void appendLastEmployeeToFile() {
        if (employeeList.isEmpty()) {
            System.out.println("No employee in memory to append.");
            return;
        }
        EmployeePayrollData last = employeeList.get(employeeList.size() - 1);
        try {
            fileIOService.appendEmployee(last);
            System.out.println("Appended employee to file: " + last);
        } catch (IOException e) {
            System.err.println("Failed to append employee: " + e.getMessage());
        }
    }

    public void appendAllEmployeesToFile() {
        if (employeeList.isEmpty()) {
            System.out.println("No employees in memory to append.");
            return;
        }
        try {
            fileIOService.appendData(employeeList);
            System.out.println("Appended " + employeeList.size() + " employees to file: " + fileIOService.getFilePath());
        } catch (IOException e) {
            System.err.println("Failed to append employees: " + e.getMessage());
        }
    }

    public void serializePayrollData() {
        try {
            fileIOService.serializeData(employeeList);
            System.out.println("Serialized payroll data to file.");
        } catch (IOException e) {
            System.err.println("Failed to serialize payroll data: " + e.getMessage());
        }
    }

    public void readSerializedPayrollData() {
        try {
            List<EmployeePayrollData> employees = fileIOService.readSerializedData();
            if (employees.isEmpty()) {
                System.out.println("No serialized payroll data found.");
                return;
            }
            System.out.println("Employees read from serialized file:");
            printFormatted(employees);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to read serialized payroll data: " + e.getMessage());
        }
    }

    private void printFormatted(List<EmployeePayrollData> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No employee payroll data to display.");
            return;
        }
        System.out.printf("%-6s %-20s %12s%n", "ID", "Name", "Salary");
        System.out.println("------------------------------------------------");
        for (EmployeePayrollData e : list) {
            System.out.printf("%-6d %-20s %12.2f%n", e.getId(), e.getName(), e.getSalary());
        }
    }

    public void analyzePayrollFromFile() {
        try {
            List<EmployeePayrollData> employees = fileIOService.readData();
            if (employees.isEmpty()) {
                System.out.println("No payroll data found in file to analyze.");
                return;
            }
            analyzeAndPrint(employees);
        } catch (IOException e) {
            System.err.println("Unable to read payroll data for analysis: " + e.getMessage());
        }
    }

    public void analyzePayrollInMemory() {
        if (employeeList.isEmpty()) {
            System.out.println("No payroll data in memory to analyze.");
            return;
        }
        analyzeAndPrint(employeeList);
    }

    private void analyzeAndPrint(List<EmployeePayrollData> list) {
        DoubleSummaryStatistics stats = list.stream().collect(Collectors.summarizingDouble(EmployeePayrollData::getSalary));
        System.out.println("Payroll Analysis:");
        System.out.println("- Number of entries: " + stats.getCount());
        System.out.printf("- Total salary: %.2f%n", stats.getSum());
        System.out.printf("- Average salary: %.2f%n", stats.getAverage());
        double min = list.stream().mapToDouble(EmployeePayrollData::getSalary).min().orElse(0.0);
        double max = list.stream().mapToDouble(EmployeePayrollData::getSalary).max().orElse(0.0);
        System.out.printf("- Min salary: %.2f%n", min);
        System.out.printf("- Max salary: %.2f%n", max);
        System.out.println("Top 3 highest paid employees:");
        list.stream()
                .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
                .limit(3)
                .forEach(e -> System.out.printf("  %d - %s: %.2f%n", e.getId(), e.getName(), e.getSalary()));
    }
}
