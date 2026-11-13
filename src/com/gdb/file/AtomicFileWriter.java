package com.gdb.file;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Provides atomic, crash-resilient file write operations using temp files and atomic rename.
 */
public class AtomicFileWriter {

    public static void write(String path, List<String> lines) throws IOException {
        Path targetPath = Paths.get(path);
        if (targetPath.getParent() != null) {
            Files.createDirectories(targetPath.getParent());
        }

        Path tempPath = Paths.get(path + ".tmp." + System.nanoTime());
        try (BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        }

        IOException lastEx = null;
        for (int attempt = 0; attempt < 5; attempt++) {
            try {
                try {
                    Files.move(tempPath, targetPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
                return;
            } catch (IOException e) {
                lastEx = e;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        try {
            Files.copy(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(tempPath);
        } catch (IOException e) {
            if (lastEx != null) throw lastEx;
            throw e;
        }
    }
}
