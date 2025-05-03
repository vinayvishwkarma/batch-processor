package com.finago.interview.task.processor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

public class XmlValidatorTest {

    private XmlValidator xmlValidator;

    @Mock
    private SchemaFactory schemaFactory;

    @Mock
    private Schema schema;

    @Mock
    private Validator validator;

    private File validXmlFile;

    private File invalidXmlFile;

    private File xsdFile;

    @BeforeEach
    void setup() throws SAXException {
        MockitoAnnotations.openMocks(this);
        xmlValidator = new XmlValidator();
        validXmlFile = mock(File.class);
        invalidXmlFile = mock(File.class);
        xsdFile = mock(File.class);
        when(schemaFactory.newSchema(xsdFile)).thenReturn(schema);
        when(schema.newValidator()).thenReturn(validator);
    }

    @Test
    void shouldReturnTrueForValidXml() throws Exception {
        doNothing().when(validator).validate(any(StreamSource.class));
        boolean isValid = xmlValidator.validate(validXmlFile, xsdFile);
        assertFalse(isValid);
        verifyNoInteractions(validator);
    }

    @Test
    void shouldReturnFalseForInvalidXml() throws Exception {
        doThrow(new SAXException("Invalid XML format")).when(validator).validate(any(StreamSource.class));
        boolean isValid = xmlValidator.validate(invalidXmlFile, xsdFile);
        assertFalse(isValid);
        verifyNoInteractions(validator);
    }

    @Test
    void shouldReturnFalseForExceptionDuringValidation() throws Exception {
        when(schemaFactory.newSchema(xsdFile)).thenThrow(new SAXException("Error with XSD"));
        boolean isValid = xmlValidator.validate(validXmlFile, xsdFile);
        assertFalse(isValid);
    }
}
