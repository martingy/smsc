package com.meltwater.smsc;

import com.meltwater.smsc.dao.GroupRepository;
import com.meltwater.smsc.dao.SubscriberRepository;
import com.meltwater.smsc.exceptions.NotExistingSubscriberException;
import com.meltwater.smsc.exceptions.NotRegisteredSubscriberException;
import com.meltwater.smsc.model.Group;
import com.meltwater.smsc.model.Subscriber;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        processInputFile(args[0]);
    }

    private static void processInputFile(String arg) throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        File file = new File(arg);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));


            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    if (StringUtils.isNotBlank(line)) {
                        String[] params = line.split(" ");
                        if (line.startsWith("number")) {
                            SubscriberRepository.add(new Subscriber(params[0], params[1]));
                        } else if (line.startsWith("subscribe")) {
                            SMSC.subscribe(params[1]);
                        } else if (line.startsWith("unsubscribe")) {
                            SMSC.subscribe(params[1]);
                        } else if (line.startsWith("group")) {
                            String[] patterns = params[1].split(",");
                            GroupRepository.add(new Group(params[0], Arrays.asList(patterns)));
                        } else if (line.startsWith("message")) {
                            String[] receivers = params[2].split(",");
                            String message = line.split("\"")[1];
                            SMSC.sendMessage(message, params[1], receivers);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error in line: " + lineNumber);
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
