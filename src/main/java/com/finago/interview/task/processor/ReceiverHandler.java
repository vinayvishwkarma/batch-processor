package com.finago.interview.task.processor;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finago.interview.task.exception.FileNotFoundProcessingException;
import com.finago.interview.task.exception.ProcessingException;
import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.ReceiverWrapper;
import com.finago.interview.task.util.ChecksumUtils;

/**
 * Processes individual Receiver entries: finds PDFs, checks MD5,
 * routes files to out/ or error/, and writes a single-entry XML.
 */
public class ReceiverHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverHandler.class);

    public void processReceiver(final Receiver receiver) throws ProcessingException {
        long receiverId = receiver.getReceiverId();
        String fileName = receiver.getFile();
        Path sourcePdf = Paths.get("data/in", fileName);
        boolean exists = Files.exists(sourcePdf);
        boolean valid = false;
        if (exists) {
            try {
                valid = ChecksumUtils.verifyMd5(sourcePdf.toFile(), receiver.getFileMd5());
            } catch (IOException e) {
                logger.error("Error reading PDF {} for MD5 check", fileName, e);
            }
        } else {
            throw new FileNotFoundProcessingException("PDF file " + fileName + " not found for receiver " + receiverId);
        }
        String base = (exists && valid) ? "out" : "error";
        long mod = receiverId % 100;
        Path targetDir = Paths.get("data", base, String.valueOf(mod), String.valueOf(receiverId));
        try {
            Files.createDirectories(targetDir);
            if (exists) {
                Files.copy(sourcePdf, targetDir.resolve(fileName), REPLACE_EXISTING);
                logger.info("Copied PDF {} to {}", fileName, targetDir);
            } else {
                logger.warn("PDF {} is missing; only writing XML", fileName);
            }
            writeSingleReceiverXml(receiver, targetDir.resolve(fileName.replaceAll("\\.pdf$", ".xml")).toFile());
        } catch (IOException e) {
            throw new ProcessingException("I/O error processing receiver " + receiverId, e);
        }
    }

    private void writeSingleReceiverXml(final Receiver receiver, final File xmlOut) throws ProcessingException {
        try {
            JAXBContext jc = JAXBContext.newInstance(ReceiverWrapper.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ReceiverWrapper wrapper = new ReceiverWrapper(receiver);
            try (FileOutputStream fos = new FileOutputStream(xmlOut)) {
                marshaller.marshal(wrapper, fos);
            }
            logger.info("Wrote receiver XML {}", xmlOut.getName());
        } catch (JAXBException | IOException e) {
            throw new ProcessingException("Failed to write receiver XML " + xmlOut.getName(), e);
        }
    }
}
