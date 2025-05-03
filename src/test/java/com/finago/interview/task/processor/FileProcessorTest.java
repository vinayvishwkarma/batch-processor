package com.finago.interview.task.processor;

import static org.mockito.ArgumentMatchers.isA;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.finago.interview.task.helper.TestHelper;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;

class FileProcessorTest {

    private FileProcessor processor;

    @Mock
    private XmlValidator validator;

    @Mock
    private XmlParser parser;

    @Mock
    private ReceiverHandler handler;

    private Path testXmlPath;

    private File testXmlFile;

    private boolean validatorResult;

    private Receivers receiversResult;

    private JAXBException jaxbException;

    @BeforeEach
    void setup() throws Exception {
        setupMockData();
        setupMocks();
        setupTestTarget();
    }

    @AfterEach
    void tearDown() throws IOException {
        Path testFilePath = Paths.get("data/in/test2.xml");
        Files.deleteIfExists(testFilePath);
    }

    private void setupMockData() throws IOException {
        Path inputFilePath = Paths.get("data/in/test2.xml");
        Files.createDirectories(inputFilePath.getParent());
        Files.createFile(inputFilePath);
        testXmlPath = Paths.get("data/in/test1.xml");
        testXmlFile = testXmlPath.toFile();
        validatorResult = true;
        jaxbException = null;
    }

    private void setupMocks() throws Exception {
        validator = Mockito.mock(XmlValidator.class);
        Mockito.when(validator.validate(isA(File.class), isA(File.class))).thenAnswer(invocation -> validatorResult);
        parser = Mockito.mock(XmlParser.class);
        Mockito.when(parser.parse(isA(File.class))).thenAnswer(invocationOnMock -> {
            if (jaxbException != null) {
                throw jaxbException;
            }
            return receiversResult;
        });
        handler = Mockito.mock(ReceiverHandler.class);
        Mockito.doAnswer(invocation -> {
            Receiver receiver = invocation.getArgument(0, Receiver.class);
            if (receiver.getFile() == null) {
                throw new IllegalArgumentException("Receiver file cannot be null");
            }
            return null;
        }).when(handler).processReceiver(isA(Receiver.class));
    }

    private void setupTestTarget() throws Exception {
        processor = new FileProcessor();
        TestHelper.injectMock(processor, "validator", validator);
        TestHelper.injectMock(processor, "parser", parser);
        TestHelper.injectMock(processor, "receiverHandler", handler);
    }

    @Test
    void shouldArchiveValidXmlAndProcessReceivers() throws Exception {
        testXmlPath = Paths.get("data/in/test1.xml");
        testXmlFile = testXmlPath.toFile();
        validatorResult = true;
        Receiver receiver = new Receiver("test.pdf");
        receiversResult = mockReceivers(receiver);
        processor.processXml(testXmlPath);
        Mockito.verify(validator).validate(testXmlFile, new File("recipient_data_schema.xsd"));
        Mockito.verify(parser).parse(testXmlFile);
        Mockito.verify(handler).processReceiver(receiver);
    }

    @Test
    void shouldMoveToErrorWhenValidationFails() throws Exception {
        testXmlPath = Paths.get("data/in/test2.xml");
        testXmlFile = testXmlPath.toFile();
        validatorResult = false;
        processor.processXml(testXmlPath);
        Mockito.verify(validator).validate(testXmlFile, new File("recipient_data_schema.xsd"));
        Mockito.verifyNoInteractions(parser);
        Mockito.verifyNoInteractions(handler);
    }

    @Test
    void shouldMoveToErrorWhenParsingFails() throws Exception {
        testXmlPath = Paths.get("data/in/test3.xml");
        testXmlFile = testXmlPath.toFile();
        validatorResult = true;
        jaxbException = new JAXBException("Mock error");
        processor.processXml(testXmlPath);
        Mockito.verify(validator).validate(testXmlFile, new File("recipient_data_schema.xsd"));
        Mockito.verify(parser).parse(testXmlFile);
        Mockito.verifyNoInteractions(handler);
    }

    private Receivers mockReceivers(final Receiver receiver) {
        Receivers receivers = new Receivers();
        receivers.setReceiver(List.of(receiver));
        return receivers;
    }
}
