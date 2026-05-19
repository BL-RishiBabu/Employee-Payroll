import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            for (EmployeePayrollData employee : employees) {
                System.out.println(employee);
            }
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
        for (EmployeePayrollData employee : employeeList) {
            System.out.println(employee);
        }
    }

    public void printEntryCount() {
        try {
            long count = fileIOService.countEntries();
            System.out.println("Number of payroll entries in file: " + count);
        } catch (IOException e) {
            System.err.println("Unable to count payroll entries: " + e.getMessage());
        }
    }
}
