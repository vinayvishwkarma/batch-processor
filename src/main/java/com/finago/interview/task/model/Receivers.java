package com.finago.interview.task.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB model representing the root <receivers> element containing multiple <receiver> entries.
 */
@XmlRootElement(name = "receivers")
public class Receivers {

    private List<Receiver> receiver = new ArrayList<>();

    @XmlElement(name = "receiver")
    public List<Receiver> getReceiver() {
        return receiver;
    }

    public void setReceiver(final List<Receiver> receiver) {
        this.receiver = receiver;
    }
}
