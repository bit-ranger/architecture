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

    private final NotificationManager notificationManager = new DefaultNotificationManager();
    private final DefaultNotificationManagerTest.InternalObserver internalObserver = new DefaultNotificationManagerTest.InternalObserver();
    private final DefaultNotificationManagerTest.ExtendInternalObserver extendInternalObserver = new DefaultNotificationManagerTest.ExtendInternalObserver();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: subscribe(Class<T> subjectType, Observer<T> internalObserver)
     */
    @Test
    public void testSubscribe() throws Exception {
        this.notificationManager.subscribe(DefaultNotificationManagerTest.InternalEvent.class, this.internalObserver);
        this.notificationManager.subscribe(DefaultNotificationManagerTest.ExtendInternalEvent.class, this.extendInternalObserver);
    }

    /**
     * Method: notify(Event event, Object subject)
     */
    @Test
    public void testNotify() throws Exception {
        this.testSubscribe();
        this.notificationManager.notify(DefaultNotificationManagerTest.ExtendInternalEvent.TEST);


        Assert.assertNotNull(this.extendInternalObserver.event);
    }

    private static class InternalObserver implements Observer<DefaultNotificationManagerTest.InternalEvent> {

        private Event event;

        @Override
        public void focus(DefaultNotificationManagerTest.InternalEvent event) {
            this.event = event;
        }
    }

    private static class ExtendInternalObserver implements Observer<DefaultNotificationManagerTest.ExtendInternalEvent> {

        private Event event;

        @Override
        public void focus(DefaultNotificationManagerTest.ExtendInternalEvent event) {
            this.event = event;
        }

    }

    private static class InternalEvent implements Event<String> {


        private final String context;

        private InternalEvent(String context) {
            this.context = context;
        }

        @Override
        public String context() {
            return this.context;
        }
    }

    private static class ExtendInternalEvent extends DefaultNotificationManagerTest.InternalEvent {
        private static final DefaultNotificationManagerTest.ExtendInternalEvent TEST = new DefaultNotificationManagerTest.ExtendInternalEvent("test");

        public ExtendInternalEvent(String context) {
            super(context);
        }
    }

} 
