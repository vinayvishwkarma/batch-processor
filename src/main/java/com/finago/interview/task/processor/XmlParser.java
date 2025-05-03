package com.finago.interview.task.processor;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finago.interview.task.model.Receivers;

/**
 * Parses a valid XML file into a Receivers object.
 */
public class XmlParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

    private final JAXBContext jaxbContext;

    public XmlParser() {
        try {
            this.jaxbContext = JAXBContext.newInstance(Receivers.class);
        } catch (JAXBException e) {
            logger.error("Failed to initialize JAXBContext for Receivers", e);
            throw new RuntimeException("Could not initialize XML parser", e);
        }
    }

    /**
     * Unmarshals the given XML file into a Receivers instance.
     * @param xmlFile the XML file to parse (must be schema-valid)
     * @return Receivers object containing a list of Receiver entries
     * @throws JAXBException if unmarshalling fails
     */
    public Receivers parse(final File xmlFile) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        logger.debug("Parsing XML file {}", xmlFile.getName());
        Receivers receivers = (Receivers) unmarshaller.unmarshal(xmlFile);
        logger.info("Parsed {} receiver(s) from {}", receivers.getReceiver().size(), xmlFile.getName());
        return receivers;
    }
}
