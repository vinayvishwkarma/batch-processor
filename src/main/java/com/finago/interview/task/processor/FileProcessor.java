package com.finago.interview.task.processor;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finago.interview.task.exception.ProcessingException;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;

/**
 * Validates, parses and processes an XML file along with its referenced PDFs.
 */
public class FileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);

    private final XmlValidator validator;

    private final XmlParser parser;

    private final ReceiverHandler receiverHandler;

    public FileProcessor() {
        this.validator = new XmlValidator();
        this.parser = new XmlParser();
        this.receiverHandler = new ReceiverHandler();
    }

    /**
     * Process a single XML file: validate, parse receivers, delegate each, then archive.
     * @param xmlPath Path to the XML file in the input directory
     */
    public void processXml(final Path xmlPath) throws ProcessingException {
        logger.info("Starting processing for XML: {}", xmlPath.getFileName());
        Path xsdPath = Paths.get("recipient_data_schema.xsd");
        File xmlFile = xmlPath.toFile();
        boolean valid;
        try {
            valid = validator.validate(xmlFile, xsdPath.toFile());
        } catch (Exception e) {
            logger.error("Validation error for {}", xmlPath.getFileName(), e);
            moveTo("error", xmlPath);
            return;
        }
        if (!valid) {
            logger.warn("XML {} failed schema validation", xmlPath.getFileName());
            moveTo("error", xmlPath);
            return;
        }
        Receivers receivers;
        try {
            receivers = parser.parse(xmlFile);
        } catch (JAXBException e) {
            logger.error("Parsing error for {}", xmlPath.getFileName(), e);
            moveTo("error", xmlPath);
            return;
        }
        List<Receiver> list = receivers.getReceiver();
        for (Receiver r : list) {
            receiverHandler.processReceiver(r);
        }
        moveTo("archive", xmlPath);
        deleteProcessedPdfs(list);
        logger.info("Completed processing for XML: {}", xmlPath.getFileName());
    }

    private void moveTo(final String folder, final Path xmlPath) {
        try {
            Path target = Paths.get("data", folder, xmlPath.getFileName().toString());
            Files.createDirectories(target.getParent());
            Files.move(xmlPath, target, REPLACE_EXISTING);
            logger.info("Moved XML {} to {}", xmlPath.getFileName(), folder);
        } catch (IOException e) {
            logger.error("Failed to move {} to {}}", xmlPath.getFileName(), folder, e);
        }
    }

    private void deleteProcessedPdfs(final List<Receiver> receivers) {
        for (Receiver r : receivers) {
            final Path pdf = Paths.get("data/in", r.getFile());
            try {
                if (Files.deleteIfExists(pdf)) {
                    logger.debug("Deleted processed PDF: {}", r.getFile());
                }
            } catch (IOException e) {
                logger.warn("Could not delete PDF: {}", r.getFile(), e);
            }
        }
    }
}
