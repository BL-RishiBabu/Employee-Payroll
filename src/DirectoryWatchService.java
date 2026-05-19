import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryWatchService implements Runnable {
    private final WatchService watcher;
    private final Path rootDir;
    private final EmployeePayrollFileIOService fileIOService;
    private final String payrollFileName;
    private volatile boolean running = true;

    public DirectoryWatchService(String directoryToWatch, EmployeePayrollFileIOService fileIOService) throws IOException {
        this.rootDir = Paths.get(directoryToWatch);
        this.fileIOService = fileIOService;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.payrollFileName = Paths.get(fileIOService.getFilePath()).getFileName().toString();

        if (Files.notExists(rootDir) || !Files.isDirectory(rootDir)) {
            throw new IOException("Directory does not exist: " + rootDir.toAbsolutePath());
        }
        registerAll(rootDir);
    }

    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void stop() {
        running = false;
        try {
            watcher.close();
        } catch (IOException ignored) {}
    }

    @Override
    public void run() {
        System.out.println("Starting directory watch on: " + rootDir.toAbsolutePath());
        while (running) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException | ClosedWatchServiceException e) {
                break;
            }

            Path dir = (Path) key.watchable();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>) event).context();
                Path child = dir.resolve(name);

                System.out.println("Event " + kind.name() + ": " + child.toAbsolutePath());

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) {
                            registerAll(child);
                            System.out.println("Registered new sub-directory: " + child.toAbsolutePath());
                        }
                    } catch (IOException ex) {
                        System.err.println("Failed to register new directory: " + ex.getMessage());
                    }
                }

                if ((kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) && Files.isRegularFile(child)) {
                    if (child.getFileName().toString().equals(payrollFileName)) {
                        try {
                            long count = fileIOService.countEntries();
                            System.out.println("Payroll file updated. Number of entries: " + count);
                        } catch (IOException ex) {
                            System.err.println("Failed to count payroll entries: " + ex.getMessage());
                        }
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }

        System.out.println("Directory watch service stopped.");
    }
}
