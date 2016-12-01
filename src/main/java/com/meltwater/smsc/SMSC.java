package com.meltwater.smsc;

import com.meltwater.smsc.dao.SubscriberRepository;
import com.meltwater.smsc.exceptions.NotExistingSubscriberException;
import com.meltwater.smsc.exceptions.NotRegisteredSubscriberException;
import com.meltwater.smsc.model.Message;
import com.meltwater.smsc.model.Subscriber;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SMSC class containing SMSC functionalities
 */
public class SMSC {

    /**
     * content format: <subscriber id> - <Subscriber>
     */
    private static ConcurrentHashMap<String, Subscriber> registeredSubscribers = new ConcurrentHashMap<String, Subscriber>();

    /**
     * content format: <subscriber (addressee) id> - <Message>
     */
    private static ConcurrentHashMap<String, Message> notDeliveredMessages = new ConcurrentHashMap<String, Message>();


    /**
     * Subscribe to the SMSC. Check if the Subscriber exists, and delivers any undelivered messages
     * @param id
     * @throws NotExistingSubscriberException
     */
    public static void subscribe(String id) throws NotExistingSubscriberException {
        Subscriber subscriber = getSubscriber(id);
        registeredSubscribers.put(subscriber.getId(), subscriber);
        deliverMessage(id);
    }

    private static Subscriber getSubscriber(String id) throws NotExistingSubscriberException {
        Subscriber subscriber = SubscriberRepository.get(id);
        if (subscriber == null) {
            throw new NotExistingSubscriberException();
        }
        return subscriber;
    }

    private static void deliverMessage(String id) {
        // Check if it has not delivered messages
        if (notDeliveredMessages.containsKey(id)) {
            Message message = notDeliveredMessages.get(id);
            try {
                handleMessageSuccess(message.getMessage(), message.getFrom(), message.getTo());
                // Message delivered
                notDeliveredMessages.remove(id);
            } catch (NotRegisteredSubscriberException e) {
                // It's unregistered in the meantime...
            }
        }
    }

    /**
     * Get a registered Subcriber from SMSC
     * @param id
     * @return
     * @throws NotRegisteredSubscriberException
     */
    public static Subscriber getRegisteredSubscriber(String id) throws NotRegisteredSubscriberException {
        Subscriber subscriber = registeredSubscribers.get(id);
        if (subscriber == null) {
            throw new NotRegisteredSubscriberException();
        }
        return subscriber;
    }

    /**
     * Unsubscribe from SMSC
     * @param id
     * @return
     * @throws NotExistingSubscriberException
     */
    public static Subscriber unSubscribe(String id) throws NotExistingSubscriberException {
        Subscriber subscriber = getSubscriber(id);
        return registeredSubscribers.remove(subscriber.getId());
    }

    public static void clearRegisteredSubscribers() {
        registeredSubscribers.clear();
    }

    public static void clearNotDeliveredMessages() {
        notDeliveredMessages.clear();
    }

    /**
     * Send a message from a Subscriber to another (or more than one).
     * Store the message if it cannot be delivered (Subscriber currently not registered but exists)
     * @param message
     * @param from
     * @param to
     * @throws NotRegisteredSubscriberException
     * @throws NotExistingSubscriberException
     */
    public static void sendMessage(String message, String from, String... to) throws NotRegisteredSubscriberException, NotExistingSubscriberException {
        for (String toE : to) {
            try {
                handleMessageSuccess(message, from, toE);
            } catch (NotRegisteredSubscriberException e) {
                // toE is not registered, does it exist?
                Subscriber subscriber = SubscriberRepository.get(toE);
                if (subscriber != null) {
                    // it exists, store the message in message queue
                    notDeliveredMessages.put(toE, new Message(from, toE, message));
                } else {
                    throw new NotExistingSubscriberException();
                }
            }
        }
    }

    private static void handleMessageSuccess(String message, String from, String toE) throws NotRegisteredSubscriberException {
        StringBuilder sb = new StringBuilder();
        sb.append(getRegisteredSubscriber(from).getPhoneNumber());
        sb.append(" ");
        sb.append(getRegisteredSubscriber(toE).getPhoneNumber());
        sb.append(" ");
        sb.append(message);

        System.out.println(sb.toString());
    }
}
