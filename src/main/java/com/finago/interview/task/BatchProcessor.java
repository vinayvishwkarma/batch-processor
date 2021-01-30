package com.finago.interview.task;

import com.finago.interview.task.model.Receiver;
import com.finago.interview.task.model.Receivers;
import com.finago.interview.task.util.AppUtil;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;

/**
 * A simple main method as an example.
 */

public class BatchProcessor {

    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        System.out.println("*beep boop* ...processing data... *beep boop*");
        Receivers receivers = AppUtil.unmarshall();
        for (Receiver receiver : receivers.getReceiver()) {
            System.out.println(receiver);
        }

    }

}
