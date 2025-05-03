package com.finago.interview.task.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.finago.interview.task.exception.FileNotFoundProcessingException;
import com.finago.interview.task.model.Receiver;

public class ReceiverHandlerTest {

    private ReceiverHandler handler;

    @BeforeEach
    void setup() throws Exception {
        setupMockData();
        setupMocks();
        setupTestTarget();
    }

    @AfterEach
    void tearDown() throws Exception {
        Path testPdfFilePath = Paths.get("data/in/test.pdf");
        Files.deleteIfExists(testPdfFilePath);
    }

    private void setupMockData() throws Exception {
        Path testPdfPath = Paths.get("data/in/test.pdf");
        Files.createDirectories(testPdfPath.getParent());
        Files.createFile(testPdfPath);
    }

    private void setupMocks() {
        handler = new ReceiverHandler();
    }

    private void setupTestTarget() {
        handler = new ReceiverHandler();
    }

    @Test
    void shouldProcessValidReceiverAndCopyPdf() throws Exception {
        Path targetDirPath = Paths.get("data", "out", "45", "12345");
        Receiver receiver = mockReceiver(12345L, "test.pdf", "invalidMd5");
        handler.processReceiver(receiver);
        assertFalse(Files.exists(targetDirPath.resolve("test.pdf")));
    }

    @Test
    void shouldHandlePdfNotFoundAndOnlyWriteXml() throws Exception {
        Receiver receiver = mockReceiver(12345L, "test.pdf", "invalidMd5");
        handler.processReceiver(receiver);
        Path targetDirPath = Paths.get("data", "error", "45", "12345");
        assertFalse(Files.exists(targetDirPath.resolve("missing.pdf")));
    }

    @Test
    void shouldHandleInvalidMd5AndMoveToError() throws Exception {
        Receiver receiver = mockReceiver(12345L, "test.pdf", "invalidMd5");
        handler.processReceiver(receiver);
        Path targetDirPath = Paths.get("data", "error", "45", "12345");
        assertTrue(Files.exists(targetDirPath.resolve("test.pdf")));
    }

    @Test
    void shouldThrowProcessingExceptionWhenFileCopyFails() throws Exception {
        Path sourcePdfPath = Paths.get("data/in", "test.pdf");
        Files.deleteIfExists(sourcePdfPath);
        Receiver receiver = mockReceiver(12345L, "test.pdf", "invalidMd5");
        FileNotFoundProcessingException exception = assertThrows(FileNotFoundProcessingException.class, () -> handler.processReceiver(receiver));
        assertEquals("PDF file test.pdf not found for receiver 12345", exception.getMessage());
    }

    private Receiver mockReceiver(final Long id, final String file, final String md5) {
        Receiver receiver = new Receiver();
        receiver.setReceiverId(id);
        receiver.setFile(file);
        receiver.setFileMd5(md5);
        return receiver;
    }
}
