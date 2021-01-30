package com.finago.interview.task.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@XmlRootElement(name = "receiver")
@XmlAccessorType(XmlAccessType.FIELD)
public class Receiver {
    @XmlElement(name = "receiver_id")
    private int receiver_id;

    @XmlElement(name = "first_name")
    private String firstName;

    @XmlElement(name = "last_name")
    private String lastName;

    @XmlElement(name = "file")
    private String fileName;

    @XmlElement(name = "file_md5")
    private String fileMD5Checksum;

}
