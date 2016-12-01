package com.meltwater.smsc;

import com.meltwater.smsc.dao.SubscriberRepository;
import com.meltwater.smsc.exceptions.NotExistingSubscriberException;
import com.meltwater.smsc.exceptions.NotRegisteredSubscriberException;
import com.meltwater.smsc.model.Subscriber;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Tests for SMSC functionalities
 */
public class SMSCTest {

    // System out test solution from: http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeTest
    public void beforeTest() {
        System.setOut(new PrintStream(outContent));
    }

    //@AfterSuite
    public void afterTest() {
        System.setOut(null);
    }

    @BeforeMethod
    public void beforeMethod() {
        SMSC.clearRegisteredSubscribers();
        SMSC.clearNotDeliveredMessages();
        SubscriberRepository.clear();
        outContent.reset();
    }

    @Test
    public void testSubscribe() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        Subscriber subscriber = addNewSubscriber();

        SMSC.subscribe(subscriber.getId());

        Assert.assertEquals(SMSC.getRegisteredSubscriber(subscriber.getId()).getId(), subscriber.getId());
        Assert.assertEquals(SMSC.getRegisteredSubscriber(subscriber.getId()).getPhoneNumber(), subscriber.getPhoneNumber());
    }

    @Test(expectedExceptions = {NotExistingSubscriberException.class})
    public void testSubscribeNotExistingSubscriber() throws NotExistingSubscriberException {
        String id = "number1";

        SMSC.subscribe(id);
    }

    @Test(expectedExceptions = {NotRegisteredSubscriberException.class})
    public void testUnSubscribe() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        String id = addNewSubscriber().getId();

        SMSC.subscribe(id);

        Assert.assertNotNull(SMSC.getRegisteredSubscriber(id));

        SMSC.unSubscribe(id);

        SMSC.getRegisteredSubscriber(id);
    }

    @Test(expectedExceptions = {NotExistingSubscriberException.class})
    public void testUnSubscribeNotExistingSubscriber() throws NotExistingSubscriberException {
        String id = "number1";

        SMSC.unSubscribe(id);
    }

    private Subscriber addNewSubscriber() {
        String phoneNumber = "06201234123";
        String id = "number1";
        return addNewSubscriber(id, phoneNumber);
    }

    private Subscriber addNewSubscriber(String id, String phoneNumber) {
        Subscriber subscriber = new Subscriber(id, phoneNumber);
        SubscriberRepository.add(subscriber);
        return subscriber;
    }

    //
    // Messaging
    //

    @Test
    public void testSendMessageToOneRegisteredSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        Subscriber subscriber1 = addNewSubscriber();
        Subscriber subscriber2 = addNewSubscriber("number2", "06201234124");

        SMSC.subscribe(subscriber1.getId());
        SMSC.subscribe(subscriber2.getId());
        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), subscriber2.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber2.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());

        Assert.assertEquals(outContent.toString(), sb.toString());
    }

    @Test(expectedExceptions = {})
    public void testSendMessageToOneNotRegisteredSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        // In this case, nothing happens... sending to a not registered subscriber shouldn't stop the process

        Subscriber subscriber1 = addNewSubscriber();
        Subscriber subscriber2 = addNewSubscriber("number2", "06201234124");

        SMSC.subscribe(subscriber1.getId());
        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), subscriber2.getId());
    }

    @Test
    public void testSendMessageToOneAppearingDisappearingRegisteredSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        Subscriber subscriber1 = addNewSubscriber();
        Subscriber subscriber2 = addNewSubscriber("number2", "06201234124");

        SMSC.subscribe(subscriber1.getId());

        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), subscriber2.getId());

        Assert.assertTrue(StringUtils.isBlank(outContent.toString()));

        SMSC.subscribe(subscriber2.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber2.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());

        Assert.assertEquals(outContent.toString(),sb.toString());
    }

    @Test(expectedExceptions = {NotExistingSubscriberException.class})
    public void testSendMessageToOneNotExistingSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        // In this case, process stops, should not send message to a not existing subscriber

        Subscriber subscriber1 = addNewSubscriber();

        SMSC.subscribe(subscriber1.getId());

        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), "number2");
    }

    @Test
    public void testSendMessageToTwoRegisteredSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        Subscriber subscriber1 = addNewSubscriber();
        Subscriber subscriber2 = addNewSubscriber("number2", "06201234124");
        Subscriber subscriber3 = addNewSubscriber("number3", "06201234125");

        SMSC.subscribe(subscriber1.getId());
        SMSC.subscribe(subscriber2.getId());
        SMSC.subscribe(subscriber3.getId());
        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), subscriber2.getId(), subscriber3.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber2.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());
        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber3.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());

        Assert.assertEquals(outContent.toString(), sb.toString());
    }

    @Test
    public void testSendMessageToOneNormalAndOneAppearingDisappearingRegisteredSubscriber() throws NotExistingSubscriberException, NotRegisteredSubscriberException {
        Subscriber subscriber1 = addNewSubscriber();
        Subscriber subscriber2 = addNewSubscriber("number2", "06201234124");
        Subscriber subscriber3 = addNewSubscriber("number3", "06201234125");

        SMSC.subscribe(subscriber1.getId());
        SMSC.subscribe(subscriber2.getId());

        String message = "Test";

        SMSC.sendMessage(message, subscriber1.getId(), subscriber2.getId(), subscriber3.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber2.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());

        Assert.assertEquals(outContent.toString(), sb.toString());

        SMSC.subscribe(subscriber3.getId());

        sb.append(subscriber1.getPhoneNumber());
        sb.append(" ");
        sb.append(subscriber3.getPhoneNumber());
        sb.append(" ");
        sb.append(message);
        sb.append(System.lineSeparator());

        Assert.assertEquals(outContent.toString(), sb.toString());
    }

    @Test
    public void testSendMessageToGroup() {
        // TODO
    }

    @Test
    public void testSendBroadcast() {
        // TODO
    }
}
