package com.finago.interview.task.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB model for individual <receiver> entries.
 */
@XmlRootElement(name = "receiver")
@XmlType(propOrder = { "receiverId", "firstName", "lastName", "file", "fileMd5" })
public class Receiver {

    private Long receiverId;

    private String firstName;

    private String lastName;

    private String file;

    private String fileMd5;

    public Receiver() {}

    public Receiver(final String file) {
        this.file = file;
    }

    @XmlElement(name = "receiver_id", required = true)
    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
    }

    @XmlElement(name = "first_name", required = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @XmlElement(name = "last_name", required = true)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @XmlElement(name = "file", required = true)
    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    @XmlElement(name = "file_md5", required = true)
    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(final String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
