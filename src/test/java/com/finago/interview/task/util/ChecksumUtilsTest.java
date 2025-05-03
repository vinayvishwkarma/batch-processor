package com.finago.interview.task.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChecksumUtilsTest {

    @TempDir
    private Path tempDir;

    @Test
    void verifyMd5ValidChecksum() throws IOException {
        File file = createTempFile("Hello World");
        String expectedMd5 = "b10a8db164e0754105b7a99be72e3fe5";
        boolean result = ChecksumUtils.verifyMd5(file, expectedMd5);
        assertTrue(result, "MD5 should match for correct content");
    }

    @Test
    void verifyMd5InvalidChecksum() throws IOException {
        File file = createTempFile("Hello World");
        String wrongMd5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        boolean result = ChecksumUtils.verifyMd5(file, wrongMd5);
        assertFalse(result, "MD5 should not match for incorrect hash");
    }

    @Test
    void verifyMd5WithIOException() {
        File nonExistentFile = new File(tempDir.toFile(), "does_not_exist.txt");
        assertThrows(IOException.class, () -> ChecksumUtils.verifyMd5(nonExistentFile, "dummy"));
    }

    private File createTempFile(String content) throws IOException {
        File file = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }
}
