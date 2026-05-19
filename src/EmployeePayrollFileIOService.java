import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
    private static final String PAYROLL_FILE = "employee_payroll_data.txt";
    private final Path payrollFilePath;

    public EmployeePayrollFileIOService() {
        this.payrollFilePath = Paths.get(PAYROLL_FILE);
    }

    public void writeData(List<EmployeePayrollData> employeeList) throws IOException {
        List<String> lines = new ArrayList<>();
        for (EmployeePayrollData employee : employeeList) {
            lines.add(employee.toString());
        }
        Files.write(payrollFilePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public List<EmployeePayrollData> readData() throws IOException {
        List<EmployeePayrollData> employees = new ArrayList<>();
        if (!Files.exists(payrollFilePath)) {
            return employees;
        }
        List<String> lines = Files.readAllLines(payrollFilePath);
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] tokens = line.split(",\s*");
            if (tokens.length == 3) {
                int id = Integer.parseInt(tokens[0]);
                String name = tokens[1];
                double salary = Double.parseDouble(tokens[2]);
                employees.add(new EmployeePayrollData(id, name, salary));
            }
        }
        return employees;
    }

    public long countEntries() throws IOException {
        if (!Files.exists(payrollFilePath)) {
            return 0;
        }
        return Files.lines(payrollFilePath).filter(line -> !line.trim().isEmpty()).count();
    }

    public String getFilePath() {
        return payrollFilePath.toAbsolutePath().toString();
    }
}
