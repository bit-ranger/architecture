package com.rainyalley.architecture.core.notification;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * DefaultNotificationManager Tester.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class DefaultNotificationManagerTest {

    private NotificationManager notificationManager = new DefaultNotificationManager();
    private InternalSubject subject = new InternalSubject(){}; // extend
    private InternalObserver observer = new InternalObserver();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: subscribe(Class<T> subjectType, Observer<T> observer)
     *
     */
    @Test
    public void testSubscribe() throws Exception {
        notificationManager.subscribe(InternalSubject.class, observer);
    }

    /**
     *
     * Method: notify(Event event, Object subject)
     *
     */
    @Test
    public void testNotify() throws Exception {
        testSubscribe();
        notificationManager.notify(InternalEvent.TEST, subject);

        Assert.assertEquals(InternalEvent.TEST, observer.event);
        Assert.assertEquals(subject, observer.subject);
    }

    private static class InternalSubject{}

    private static class InternalObserver implements Observer<InternalSubject>{

        private Event event;
        private InternalSubject subject;

        @Override
        public void focus(Event event, InternalSubject subject) {
            this.event = event;
            this.subject = subject;
        }
    }

    private static enum  InternalEvent implements Event{
        TEST
    }

} 
