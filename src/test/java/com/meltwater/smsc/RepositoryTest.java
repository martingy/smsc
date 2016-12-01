package com.meltwater.smsc;

import com.meltwater.smsc.dao.GroupRepository;
import com.meltwater.smsc.dao.SubscriberRepository;
import com.meltwater.smsc.model.Group;
import com.meltwater.smsc.model.Subscriber;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for Repositories
 */
public class RepositoryTest {

    @Test
    public void testNewSubscriber() {
        SubscriberRepository.clear();

        String phoneNumber = "06201234123";
        String id = "number1";

        SubscriberRepository.add(new Subscriber(id, phoneNumber));

        Assert.assertEquals(SubscriberRepository.get(id).getId(), id);
        Assert.assertEquals(SubscriberRepository.get(id).getPhoneNumber(), phoneNumber);
    }

    @Test
    public void testNewGroup() {
        GroupRepository.clear();

        List<String> phoneNumberPattern = Arrays.asList("0620123*");
        String id = "group1";

        GroupRepository.add(new Group(id, phoneNumberPattern));

        Assert.assertEquals(GroupRepository.get(id).getId(), id);
        Assert.assertEquals(GroupRepository.get(id).getPhoneNumberPattern(), phoneNumberPattern);
    }

}
