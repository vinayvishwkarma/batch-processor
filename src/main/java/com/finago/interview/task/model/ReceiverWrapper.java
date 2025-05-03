package com.finago.interview.task.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper to produce a single <receivers> root element containing one <receiver>.
 */
@XmlRootElement(name = "receivers")
public class ReceiverWrapper {

    private Receiver receiver;

    public ReceiverWrapper() {}

    public ReceiverWrapper(final Receiver receiver) {
        this.receiver = receiver;
    }

    @XmlElement(name = "receiver")
    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(final Receiver receiver) {
        this.receiver = receiver;
    }
}
