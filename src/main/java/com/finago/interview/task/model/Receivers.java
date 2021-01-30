package com.finago.interview.task.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "receivers")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class Receivers {
    @XmlElement
    private List<Receiver> receiver;
}
