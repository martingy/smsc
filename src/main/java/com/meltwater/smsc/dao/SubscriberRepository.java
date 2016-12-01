package com.meltwater.smsc.dao;

import com.meltwater.smsc.model.Subscriber;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Repo containing Subscribers
 */
public class SubscriberRepository {

    private static ConcurrentHashMap<String, Subscriber> repository = new ConcurrentHashMap<String, Subscriber>();

    public static void add(Subscriber s) {
        repository.put(s.getId(), s);
    }

    public static Subscriber remove(Subscriber s) {
        return repository.remove(s.getId());
    }

    public static Subscriber get(String id) {
        return repository.get(id);
    }

    public static void clear() {
        repository.clear();
    }
}
