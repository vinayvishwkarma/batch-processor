package com.finago.interview.task.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for checksum calculations.
 */
public class ChecksumUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChecksumUtils.class);

    private ChecksumUtils() {}

    /**
     * Verifies the MD5 checksum of a file against the expected hash.
     *
     * @param file         The file to compute checksum for.
     * @param expectedHash The expected MD5 hash (hex string).
     * @return true if the computed MD5 matches expectedHash (case-insensitive).
     * @throws IOException if reading the file fails.
     */
    public static boolean verifyMd5(final File file, final String expectedHash) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder(2 * digest.length);
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            String computed = sb.toString();
            boolean matches = computed.equalsIgnoreCase(expectedHash);
            if (!matches) {
                logger.warn("MD5 mismatch for {}: expected={}, actual={}", file.getName(), expectedHash, computed);
            }
            return matches;
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 algorithm not available", e);
            return false;
        }
    }
}
