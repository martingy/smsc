package com.meltwater.smsc.dao;

import com.meltwater.smsc.model.Group;
import com.meltwater.smsc.model.Subscriber;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Repo containing Groups
 */
public class GroupRepository {

    private static ConcurrentHashMap<String, Group> repository = new ConcurrentHashMap<String, Group>();

    public static void add(Group s) {
        repository.put(s.getId(), s);
    }

    public static Group remove(Group s) {
        return repository.remove(s.getId());
    }

    public static Group get(String id) {
        return repository.get(id);
    }

    public static void clear() {
        repository.clear();
    }
}
