import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperationsService {
    public void createDirectory(String directoryName) {
        Path directoryPath = Paths.get(directoryName);
        try {
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("Directory created: " + directoryPath.toAbsolutePath());
            } else {
                System.out.println("Directory already exists: " + directoryPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    public void createFile(String directoryName, String fileName) {
        Path filePath = Paths.get(directoryName, fileName);
        try {
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            } else {
                System.out.println("File already exists: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create file: " + e.getMessage());
        }
    }

    public void deleteFile(String directoryName, String fileName) {
        Path filePath = Paths.get(directoryName, fileName);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted: " + filePath.toAbsolutePath());
            } else {
                System.out.println("File does not exist: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
        }
    }

    public void checkFileExists(String directoryName, String fileName) {
        Path filePath = Paths.get(directoryName, fileName);
        if (Files.exists(filePath)) {
            System.out.println("File exists: " + filePath.toAbsolutePath());
        } else {
            System.out.println("File does not exist: " + filePath.toAbsolutePath());
        }
    }

    public void listFiles(String directoryName, String extension) {
        Path directoryPath = Paths.get(directoryName);
        if (Files.notExists(directoryPath) || !Files.isDirectory(directoryPath)) {
            System.out.println("Directory does not exist: " + directoryPath.toAbsolutePath());
            return;
        }
        System.out.println("Files in directory " + directoryPath.toAbsolutePath() + " with extension '" + extension + "':");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*." + extension)) {
            boolean found = false;
            for (Path path : stream) {
                System.out.println("- " + path.getFileName());
                found = true;
            }
            if (!found) {
                System.out.println("No files found with extension: " + extension);
            }
        } catch (IOException e) {
            System.err.println("Failed to list files: " + e.getMessage());
        }
    }

    public void listDirectoryContents(String directoryName) {
        Path directoryPath = Paths.get(directoryName);
        if (Files.notExists(directoryPath) || !Files.isDirectory(directoryPath)) {
            System.out.println("Directory does not exist: " + directoryPath.toAbsolutePath());
            return;
        }
        System.out.println("Directory contents for " + directoryPath.toAbsolutePath() + ":");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            boolean found = false;
            for (Path path : stream) {
                System.out.println("- " + (Files.isDirectory(path) ? "[DIR] " : "[FILE] ") + path.getFileName());
                found = true;
            }
            if (!found) {
                System.out.println("The directory is empty.");
            }
        } catch (IOException e) {
            System.err.println("Failed to list directory contents: " + e.getMessage());
        }
    }
}
