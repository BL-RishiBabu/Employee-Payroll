import java.io.IOException;
import java.util.Scanner;

public class EmployeePayrollMain {
    public static void main(String[] args) {
        EmployeePayrollService payrollService = new EmployeePayrollService();
        FileOperationsService fileOperationsService = new FileOperationsService();
        EmployeePayrollFileIOService fileIOService = new EmployeePayrollFileIOService();
        Scanner scanner = new Scanner(System.in);

        DirectoryWatchService directoryWatchService = null;
        Thread watchThread = null;

        System.out.println("=== Employee Payroll Service ===");
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Add employee payroll record");
            System.out.println("2. Print current payroll records");
            System.out.println("3. Write payroll data to file");
            System.out.println("4. Read payroll data from file");
            System.out.println("5. Count payroll records in file");
            System.out.println("6. Create a directory (UC-2)");
            System.out.println("7. Create a file inside a directory (UC-2)");
            System.out.println("8. Delete a file (UC-2)");
            System.out.println("9. Check if a file exists (UC-2)");
            System.out.println("10. List files by extension in a directory (UC-2)");
            System.out.println("11. List directory contents (UC-2)");
            System.out.println("12. Start watching a directory (UC-3)");
            System.out.println("13. Stop watching directory (UC-3)");
            System.out.println("14. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> payrollService.addEmployeeFromConsole(scanner);
                case "2" -> payrollService.printPayrollData();
                case "3" -> payrollService.writePayrollDataToFile();
                case "4" -> payrollService.readPayrollDataFromFile();
                case "5" -> payrollService.printEntryCount();
                case "6" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    fileOperationsService.createDirectory(directoryName);
                }
                case "7" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    System.out.print("Enter file name: ");
                    String fileName = scanner.nextLine().trim();
                    fileOperationsService.createFile(directoryName, fileName);
                }
                case "8" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    System.out.print("Enter file name: ");
                    String fileName = scanner.nextLine().trim();
                    fileOperationsService.deleteFile(directoryName, fileName);
                }
                case "9" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    System.out.print("Enter file name: ");
                    String fileName = scanner.nextLine().trim();
                    fileOperationsService.checkFileExists(directoryName, fileName);
                }
                case "10" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    System.out.print("Enter file extension (without dot): ");
                    String extension = scanner.nextLine().trim();
                    fileOperationsService.listFiles(directoryName, extension);
                }
                case "11" -> {
                    System.out.print("Enter directory name: ");
                    String directoryName = scanner.nextLine().trim();
                    fileOperationsService.listDirectoryContents(directoryName);
                }
                case "12" -> {
                    System.out.print("Enter directory to watch: ");
                    String directoryToWatch = scanner.nextLine().trim();
                    if (directoryWatchService != null && watchThread != null && watchThread.isAlive()) {
                        System.out.println("A watch is already running. Stop it before starting a new one.");
                    } else {
                        try {
                            directoryWatchService = new DirectoryWatchService(directoryToWatch, fileIOService);
                            watchThread = new Thread(directoryWatchService);
                            watchThread.start();
                        } catch (IOException e) {
                            System.err.println("Failed to start directory watch: " + e.getMessage());
                        }
                    }
                }
                case "13" -> {
                    if (directoryWatchService != null) {
                        directoryWatchService.stop();
                        try {
                            if (watchThread != null) watchThread.interrupt();
                        } catch (Exception ignored) {}
                        directoryWatchService = null;
                        watchThread = null;
                        System.out.println("Stopped directory watch.");
                    } else {
                        System.out.println("No active directory watch to stop.");
                    }
                }
                case "14" -> {
                    running = false;
                    System.out.println("Exiting Employee Payroll Service.");
                }
                default -> System.out.println("Invalid option. Please choose a number from 1 to 14.");
            }
        }

        // Ensure watcher is stopped before exit
        if (directoryWatchService != null) {
            directoryWatchService.stop();
            if (watchThread != null) watchThread.interrupt();
        }

        scanner.close();
    }
}
