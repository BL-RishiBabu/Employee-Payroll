# Employee Payroll Service

## Overview

This project is an Employee Payroll Service built using Java I/O concepts. It demonstrates file operations, employee data management, and directory monitoring. The application can read and write employee payroll records to a file, count entries, display stored employee data, and monitor filesystem changes.

## Features

- Create and manage employee payroll entries
- Store employee details in a persistent text file
- Read payroll data from file and display it in the console
- Print the number of employee entries stored in the file
- Count and validate file records
- Watch a directory for file events and subdirectory changes

## Use Cases

- **UC1**: Read and write employee payroll data from/to the console and file
- **UC2**: Demonstrate Java file operations such as create, delete, and list files/directories
- **UC3**: Watch a directory to detect changes in files and subdirectories
- **UC4**: Store employee payroll objects into a file with proper serialization
- **UC5**: Print employee payroll records and count the number of entries
- **UC6**: Read employee payroll file data for analysis and validation

## Project Structure

- `src/` - Java source files for employee payroll operations
- `README.md` - Project documentation
- `employee_payroll_data.txt` (or similar) - Payroll data file used by the application

## How to Run

1. Open the project in a Java IDE or terminal.
2. Compile the Java source files.
3. Run the main class that initiates the employee payroll service.
4. Use the console prompts to add, view, or count payroll entries.

## Example Commands

```sh
javac -d bin src/**/*.java
java -cp bin com.employee.payroll.Main
```

## Notes

- Ensure the file path used by the application is writable.
- The watch service relies on Java NIO, so it can monitor live changes inside the target directory.
- The payroll service is designed for learning Java I/O and file management concepts.

## Author

Employee Payroll Project created as part of BridgeLabz training.

## License

This repository is provided for educational purposes.
