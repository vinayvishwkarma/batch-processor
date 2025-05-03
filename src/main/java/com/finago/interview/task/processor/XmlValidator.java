package com.finago.interview.task.processor;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates an XML file against the provided XSD schema.
 */
public class XmlValidator {

    private static final Logger logger = LoggerFactory.getLogger(XmlValidator.class);

    /**
     * Validates the given XML file against the provided XSD schema.
     * @param xmlFile XML file to validate
     * @param xsdFile XSD schema file to validate against
     * @return true if valid, false if not
     */
    public boolean validate(final File xmlFile, final File xsdFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            logger.debug("XML {} is valid against schema {}", xmlFile.getName(), xsdFile.getName());
            return true;
        } catch (Exception e) {
            logger.warn("XML {} failed validation against {}: {}", xmlFile.getName(), xsdFile.getName(), e.getMessage());
            return false;
        }
    }
}
