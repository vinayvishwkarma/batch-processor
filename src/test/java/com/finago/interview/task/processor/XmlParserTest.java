package com.finago.interview.task.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;

class XmlParserTest {

    private XmlParser xmlParser;

    @Mock
    private JAXBContext jaxbContext;

    @Mock
    private Unmarshaller unmarshaller;

    private File validXmlFile;

    private File invalidXmlFile;

    private Receivers receivers;

    @BeforeEach
    void setup() throws JAXBException {
        setupMockData();
        setupMocks();
        setupTarget();
    }

    private void setupMockData() throws JAXBException {
        validXmlFile = new File("src/test/resources/valid_receivers.xml");
        invalidXmlFile = new File("src/test/resources/invalid_receivers.xml");
        createSampleXmlFile(validXmlFile, true);
        createSampleXmlFile(invalidXmlFile, false);
    }

    private void setupMocks() throws JAXBException {
        jaxbContext = Mockito.mock(JAXBContext.class);
        unmarshaller = Mockito.mock(Unmarshaller.class);
        when(jaxbContext.createUnmarshaller()).thenReturn(unmarshaller);
        when(unmarshaller.unmarshal(validXmlFile)).thenAnswer(invocation -> receivers);
        when(unmarshaller.unmarshal(invalidXmlFile)).thenThrow(new JAXBException("Invalid XML format"));
    }

    private void setupTarget() {
        xmlParser = new XmlParser();
    }

    @Disabled("Temporarily disabled for debugging")
    @Test
    void shouldParseValidXmlCorrectly() throws Exception {
        receivers = new Receivers();
        receivers.setReceiver(Arrays.asList(mockReceiver(12345L, "test.pdf", "valid_md5_hash"),
                mockReceiver(67890L, "test2.pdf", "valid_md5_hash")));
        Receivers parsedReceivers = xmlParser.parse(validXmlFile);
        assertNotNull(parsedReceivers, "not null");
        assertSame(receivers, parsedReceivers, "same receivers");
        verify(unmarshaller, times(1)).unmarshal(validXmlFile);
    }

    @Disabled("Temporarily disabled for debugging")
    @Test
    void shouldThrowJAXBExceptionWhenXmlIsInvalid() throws Exception {
        JAXBException thrownException = assertThrows(JAXBException.class, () -> {
            xmlParser.parse(invalidXmlFile);
        });
        assertEquals("Invalid XML format", thrownException.getMessage());
    }

    @Disabled("Temporarily disabled for debugging")
    @Test
    void shouldLogErrorWhenJAXBContextInitializationFails() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            new XmlParser();
        });
        assertEquals("Could not initialize XML parser", runtimeException.getMessage());
    }

    private void createSampleXmlFile(File file, boolean valid) {
        try {
            if (valid) {
                // Create a valid XML content
                String validXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<receivers>\n" +
                        "  <receiver>\n" +
                        "    <receiverId>12345</receiverId>\n" +
                        "    <file>test.pdf</file>\n" +
                        "    <fileMd5>valid_md5_hash</fileMd5>\n" +
                        "  </receiver>\n" +
                        "</receivers>";
                writeToFile(file, validXmlContent);
            } else {
                // Create an invalid XML content (e.g., missing closing tag)
                String invalidXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<receivers>\n" +
                        "  <receiver>\n" +
                        "    <receiverId>12345</receiverId>\n" +
                        "    <file>test.pdf</file>\n" +
                        "    <fileMd5>invalid_md5_hash</fileMd5>\n";
                writeToFile(file, invalidXmlContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(File file, String content) throws Exception {
        file.getParentFile().mkdirs();
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(content);
        }
    }

    private Receiver mockReceiver(final Long id, final String file, final String md5) {
        Receiver receiver = new Receiver();
        receiver.setReceiverId(id);
        receiver.setFile(file);
        receiver.setFileMd5(md5);
        return receiver;
    };
}
