import java.util.Scanner;

public class EmployeePayrollMain {
    public static void main(String[] args) {
        EmployeePayrollService payrollService = new EmployeePayrollService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Employee Payroll Service (UC-1) ===");
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Add employee payroll record");
            System.out.println("2. Print current payroll records");
            System.out.println("3. Write payroll data to file");
            System.out.println("4. Read payroll data from file");
            System.out.println("5. Count payroll records in file");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> payrollService.addEmployeeFromConsole(scanner);
                case "2" -> payrollService.printPayrollData();
                case "3" -> payrollService.writePayrollDataToFile();
                case "4" -> payrollService.readPayrollDataFromFile();
                case "5" -> payrollService.printEntryCount();
                case "6" -> {
                    running = false;
                    System.out.println("Exiting Employee Payroll Service.");
                }
                default -> System.out.println("Invalid option. Please choose a number from 1 to 6.");
            }
        }

        scanner.close();
    }
}
